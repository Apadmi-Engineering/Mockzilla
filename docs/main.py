import os
import glob
from pathlib import Path
import re
import platform

download_site_url = "https://install.mockzilla.apadmi.dev"
def define_env(env):
  "Hook function"

  def remove_prefix(text, prefix):
       return text[text.startswith(prefix) and len(prefix):]

  @env.macro
  def get_download_site_url():
    return download_site_url

  @env.macro
  def print_source_file(filename, indent = ""):
      full_path = glob.glob(f'../**/{filename}', recursive=True)[0]

      return Path(full_path).read_text().replace('\n', '\n' + indent)

  @env.macro
  def get_version():
      build_gradle_text = print_source_file("mockzilla/build.gradle.kts")

      version_pattern = r'version\s*=.*"(.*\..*\..*)"'
      match = re.search(version_pattern, build_gradle_text)
      if match:
          return match.group(1)
      else:
          return None

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