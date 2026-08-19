import os
import glob
import shutil
from pathlib import Path
import re
import platform

download_site_url = "https://install.mockzilla.apadmi.dev"
site_url = "https://mockzilla.apadmi.dev"

# Consumer-facing docs pages, in reading order. The Contributing pages are
# intentionally excluded — the llms.txt / llms-full.txt outputs target
# consumers of the published libraries, not contributors to this repo.
consumer_pages = [
    "index.md",
    "quick-start.md",
    "endpoints.md",
    "web.md",
    "additional_config.md",
    "browser_stack.md",
    "snapshots.md",
    "desktop/overview.md",
    "mobile_ui.md",
    "presets.md",
]

# High-value correctness signals for LLM coding assistants, embedded at the top
# of both generated files (things an assistant is otherwise prone to get wrong).
llms_preamble = """\
Mockzilla is a compile-safe mock HTTP server that runs embedded inside your mobile app during development and testing, letting you mock API responses without a real backend. It targets Android, iOS, Kotlin Multiplatform, Flutter and browser (JS/MSW) apps.

Install coordinates:
- Kotlin / Android / Kotlin Multiplatform (Maven Central): `com.apadmi:mockzilla` (plus `com.apadmi:mockzilla-common` for the shared models/DSL, and `com.apadmi:mockzilla-mobile-ui` for the embeddable in-app overlay).
- Flutter (pub.dev): `mockzilla`.
- Native iOS: `SwiftMockzilla` via Swift Package Manager (`https://github.com/Apadmi-Engineering/SwiftMockzilla.git`) or CocoaPods.

Important notes for code generation:
- There is NO standalone npm / JavaScript package. Do not use `npm install mockzilla`. Browser support (JS, backed by Mock Service Worker) is only reachable through the Flutter `mockzilla_web` plugin or the Kotlin/JS `com.apadmi:mockzilla` target.
- Mockzilla is a development and testing tool only. It must never be shipped to production.
"""


def _print_source_file(filename, indent=""):
    full_path = glob.glob(f'../**/{filename}', recursive=True)[0]
    return Path(full_path).read_text().replace('\n', '\n' + indent)


def _extract_version(build_gradle_path):
    text = _print_source_file(build_gradle_path)
    match = re.search(r'version\s*=.*"(.*\..*\..*)"', text)
    return match.group(1) if match else None


def _get_version():
    return _extract_version("mockzilla/build.gradle.kts")


def _get_mobile_ui_version():
    return _extract_version("mockzilla-management-ui/mockzilla-mobile-ui/build.gradle.kts")


def define_env(env):
  "Hook function"

  @env.macro
  def get_download_site_url():
    return download_site_url

  @env.macro
  def print_source_file(filename, indent = ""):
      return _print_source_file(filename, indent)

  @env.macro
  def get_mobile_ui_version():
      return _get_mobile_ui_version()

  @env.macro
  def get_version():
      return _get_version()

  @env.macro
  def get_python_version():
        return platform.python_version()

def update_download_file():
  # Define your multiline string with placeholders for the variables
  multiline_string = f"""
<!DOCTYPE HTML>
<!-- Adapted from: https://stackoverflow.com/a/5411601/8474597 -->
<html lang="en-GB">
<head>
  <meta charset="UTF-8">
  <meta http-equiv="refresh" content="0; url={download_site_url}">
  <script type="text/javascript">
      window.location.href = "{download_site_url}"
  </script>
  <title>Page Redirection</title>
</head>
<body>
If you are not redirected automatically, follow this <a href='{download_site_url}'>link</a>.
</body>
</html>
  """

  # Write the multiline string to the specified file
  with open("docs/download.html", 'w') as file:
      file.write(multiline_string)


# Resolvers for the no-argument macros that appear in the consumer pages, used
# to turn `{{ get_version() }}`-style tokens into real values in llms-full.txt.
_macro_resolvers = {
    "get_version()": _get_version,
    "get_mobile_ui_version()": _get_mobile_ui_version,
    "get_download_site_url()": lambda: download_site_url,
}


def _resolve_macros(text):
    def replace(match):
        resolver = _macro_resolvers.get(match.group(1).strip())
        return str(resolver()) if resolver else match.group(0)
    return re.sub(r'\{\{\s*(.*?)\s*\}\}', replace, text)


def _parse_frontmatter(block):
    data = {}
    for line in block.splitlines():
        match = re.match(r'\s*([A-Za-z_]+):\s*(.*)', line)
        if match:
            data[match.group(1)] = match.group(2).strip()
    return data


def _split_frontmatter(text):
    if text.startswith("---"):
        parts = text.split("---", 2)
        if len(parts) == 3:
            return _parse_frontmatter(parts[1]), parts[2].lstrip("\n")
    return {}, text


def _page_title(frontmatter, body, page):
    if frontmatter.get("title"):
        return frontmatter["title"]
    match = re.search(r'^#\s+(.*)', body, re.MULTILINE)
    return match.group(1).strip() if match else page[:-len(".md")]


def _page_md_url(page):
    # Clean per-page Markdown lives alongside the HTML, mirroring the source path
    # (e.g. `desktop/overview.md` -> https://mockzilla.apadmi.dev/desktop/overview.md).
    return f"{site_url}/{page}"


# Link targets allowed to be non-`.md` internal references: images/assets and the
# Dokka API reference (generated HTML with no Markdown equivalent).
_allowed_link_suffixes = (".md", ".png", ".jpg", ".jpeg", ".gif", ".webp", ".svg", ".mp4")


def _iter_link_targets(body):
    """Yield the URL of every Markdown inline link/image (`[txt](url)` / `![alt](url)`)."""
    for match in re.finditer(r'\]\(([^)]+)\)', body):
        # Drop an optional "title" after the URL: [x](url "title").
        yield match.group(1).split()[0]


def _html_doc_link_violations(body):
    """Return internal links written in HTML form (e.g. `/endpoints/`, `../quick-start/`)
    instead of the required relative `.md` form.

    These resolve to rendered HTML pages, which breaks the clean-Markdown corpus served
    to LLM assistants — internal doc links must be relative `.md` links so they work in
    both the Zensical HTML build and the generated per-page `.md` files."""
    violations = []
    for target in _iter_link_targets(body):
        base = target.split('#', 1)[0]
        if not base or target.startswith(('http://', 'https://', 'mailto:', '#')):
            continue
        if 'dokka' in base or base.endswith(_allowed_link_suffixes):
            continue
        violations.append(target)
    return violations


def _write_robots_txt(output_dir):
    """Write robots.txt pointing crawlers at the sitemap (Zensical emits no robots.txt)."""
    content = (
        "User-agent: *\n"
        "Allow: /\n"
        "\n"
        f"Sitemap: {site_url}/sitemap.xml\n"
    )
    Path(output_dir, "robots.txt").write_text(content)


# `overrides/` (the theme's custom_dir templates, which include a full standalone copy of the
# homepage) sits inside the docs tree and gets copied verbatim into the build output even
# though it's not a real nav page — not in the nav or sitemap and linked from nowhere.
# Hopefully we can get rid of this after https://github.com/zensical/backlog/issues/65 is finished
_orphan_output_dirs = ("overrides",)


def _prune_orphan_output(output_dir):
    for name in _orphan_output_dirs:
        path = Path(output_dir, name)
        if path.exists():
            shutil.rmtree(path)


def generate_llms_files(docs_dir="docs", output_dir="site"):
    """Generate llms.txt and llms-full.txt (https://llmstxt.org) into the built
    site directory so the Cloudflare Pages deploy serves them at the site root.

    Run after `zensical build`, from the `docs/` directory."""
    os.makedirs(output_dir, exist_ok=True)

    site_description = ""
    index_lines = []
    full_sections = []
    link_errors = {}

    for page in consumer_pages:
        frontmatter, body = _split_frontmatter(Path(docs_dir, page).read_text())
        title = _page_title(frontmatter, body, page)
        description = frontmatter.get("description", "")

        # The homepage (template: home.html) has an empty Markdown body, so it
        # yields no useful per-page doc — capture its description for the blockquote
        # and skip it everywhere else.
        if page == "index.md":
            site_description = description
            continue

        body = _resolve_macros(body).strip()
        # Ensure each standalone page opens with a title for context when read alone.
        if not body.startswith("#"):
            body = f"# {title}\n\n{body}"

        # Guard the convention: internal doc links must be relative .md links.
        violations = _html_doc_link_violations(body)
        if violations:
            link_errors[page] = violations

        # Write the clean per-page Markdown next to the HTML, creating nested dirs
        # (e.g. site/desktop/) as needed.
        md_path = Path(output_dir, page)
        md_path.parent.mkdir(parents=True, exist_ok=True)
        md_path.write_text(body)

        url = _page_md_url(page)
        index_lines.append(
            f"- [{title}]({url}): {description}" if description else f"- [{title}]({url})"
        )
        full_sections.append(f"<!-- Page: {title} — {url} -->\n\n{body}")

    if link_errors:
        detail = "\n".join(
            f"  {page}: {', '.join(bad)}" for page, bad in link_errors.items()
        )
        raise ValueError(
            "Internal doc links must be relative .md links, not HTML paths, so they "
            "resolve in both the HTML site and the generated .md files. Offending "
            f"links:\n{detail}"
        )

    llms_txt = "\n".join([
        "# Mockzilla",
        "",
        f"> {site_description}",
        "",
        llms_preamble,
        "## Docs",
        "",
        *index_lines,
        "",
        "## Optional",
        "",
        f"- [API Reference (Kotlin KDoc)]({site_url}/dokka/): Generated Dokka API reference for the Kotlin modules.",
        "- [GitHub repository](https://github.com/Apadmi-Engineering/Mockzilla): Source, samples and issue tracker.",
        "",
    ])

    llms_full = "\n".join([
        "# Mockzilla — Full Documentation",
        "",
        f"> {site_description}",
        "",
        llms_preamble,
        "\n\n---\n\n".join(full_sections),
        "",
    ])

    Path(output_dir, "llms.txt").write_text(llms_txt)
    Path(output_dir, "llms-full.txt").write_text(llms_full)

    # SEO finalisation of the built site: crawler directives + dropping the orphan/duplicate
    # output the static build leaks. Done here because this is the post-`zensical build` hook
    # that already owns the `site` output directory (see fastlane/fastfiles/docs.rb).
    _write_robots_txt(output_dir)
    _prune_orphan_output(output_dir)
