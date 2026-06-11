import { build } from 'vite'
import { readFileSync, writeFileSync, readdirSync } from 'fs'
import { resolve, dirname } from 'path'
import { fileURLToPath, pathToFileURL } from 'url'

const __dirname = dirname(fileURLToPath(import.meta.url))
const outputPath = resolve(__dirname, '../docs/overrides/homepage-content.html')

// Read version from gradle; VITE_VERSION_NAME env var takes precedence (set by CI via Fastlane).
const gradleText = readFileSync(resolve(__dirname, '../../mockzilla/build.gradle.kts'), 'utf-8')
const versionMatch = gradleText.match(/"(.*?)" \/\/ x-release-please-version/)
process.env.VITE_VERSION_NAME ||= versionMatch ? versionMatch[1] : 'Dev'

// Collapses whitespace and strips single-line comments — sufficient for small inline scripts.
const minify = src => src.replace(/\/\/[^\n]*/g, '').replace(/\s+/g, ' ').trim()

// Reads Material's /.__palette key before first paint to prevent FOUC.
// Homepage and docs share this single key so their dark-mode states are always in sync.
const themeInitScript = minify(`
  try {
    var p = localStorage.getItem('/.__palette');
    var dark = p
      ? JSON.parse(p).index === 1
      : window.matchMedia('(prefers-color-scheme:dark)').matches;
    if (dark) document.documentElement.classList.add('dark');
  } catch (e) {}
`)

// Wires up the toggle button and writes Material's /.__palette key on click.
const toggleScript = minify(`
  (function () {
    var b = document.getElementById('theme-toggle');
    if (!b) return;
    b.addEventListener('click', function () {
      var dark = document.documentElement.classList.toggle('dark');
      try {
        localStorage.setItem('/.__palette', JSON.stringify({ index: dark ? 1 : 0 }));
      } catch (e) {}
    });
  })()
`)

// Step 1: Browser build — uses vite.config.mjs (SWC + Tailwind) to compile CSS.
console.log('Building assets...')
await build({ logLevel: 'warn' })

// Step 2: SSR build — uses @vitejs/plugin-react (standard Babel, not SWC) because the SWC
// plugin doesn't reliably produce Node.js-runnable output. react-syntax-highlighter is bundled
// inline (noExternal) to avoid CJS/ESM interop errors when it's externalized.
console.log('Building SSR bundle...')
const { default: reactPlugin } = await import('@vitejs/plugin-react')
await build({
  configFile: false,
  plugins: [reactPlugin()],
  resolve: {
    extensions: ['.js', '.jsx', '.ts', '.tsx', '.json'],
    alias: { '@': resolve(__dirname, './src') },
  },
  logLevel: 'warn',
  ssr: { noExternal: ['react-syntax-highlighter'] },
  build: {
    ssr: 'src/ssr-entry.tsx',
    outDir: 'dist-ssr',
    rollupOptions: { output: { format: 'esm' } },
  },
})

// Step 3: Run the SSR bundle to get pre-rendered HTML.
// The cache-bust query param (?t=...) forces Node to re-import on repeated runs.
console.log('Rendering HTML...')
const ssrEntryPath = resolve(__dirname, 'dist-ssr/ssr-entry.js')
const { render } = await import(`${pathToFileURL(ssrEntryPath).href}?t=${Date.now()}`)
const bodyHtml = render()

// Step 4: Inline the compiled CSS.
const assetsDir = resolve(__dirname, 'build/homepage-assets')
const css = readdirSync(assetsDir)
  .filter(f => f.endsWith('.css'))
  .map(f => readFileSync(resolve(assetsDir, f), 'utf-8'))
  .join('\n')

// Step 5: Write the complete standalone HTML document.
const title = 'Mockzilla — Build API mocks with ease'
const desc = 'A compile-safe solution for running and configuring a local HTTP server for your mobile apps. Supports Android, iOS, Kotlin Multiplatform and Flutter.'
const ogUrl = 'https://mockzilla.apadmi.dev/'

const fullDocument = `<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>${title}</title>
<link rel="canonical" href="${ogUrl}">
<meta name="description" content="${desc}">
<meta property="og:type" content="website">
<meta property="og:site_name" content="Mockzilla">
<meta property="og:title" content="${title}">
<meta property="og:description" content="${desc}">
<meta property="og:url" content="${ogUrl}">
<script>${themeInitScript}</script>
<style>
${css}
</style>
</head>
<body>
${bodyHtml}
<script>${toggleScript}</script>
</body>
</html>`

writeFileSync(outputPath, fullDocument)
console.log(`✓ Standalone HTML document written to ${outputPath}`)
