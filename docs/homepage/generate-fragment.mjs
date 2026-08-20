import { execFileSync } from 'child_process'
import { readFileSync, writeFileSync } from 'fs'
import { dirname, resolve } from 'path'
import { fileURLToPath } from 'url'

const __dirname = dirname(fileURLToPath(import.meta.url))
const outputPath = resolve(__dirname, '../docs/overrides/homepage-content.html')

// Read version from gradle; MOCKZILLA_VERSION_NAME env var takes precedence (set by CI via Fastlane).
const gradleText = readFileSync(resolve(__dirname, '../../mockzilla/build.gradle.kts'), 'utf-8')
const versionMatch = gradleText.match(/"(.*?)" \/\/ x-release-please-version/)
const version = process.env.MOCKZILLA_VERSION_NAME || (versionMatch ? versionMatch[1] : 'Dev')

// Compile the Tailwind utility classes used in src/template.html into plain CSS.
console.log('Compiling CSS...')
const tailwindCli = resolve(__dirname, 'node_modules/.bin/tailwindcss')
const css = execFileSync(tailwindCli, ['-i', 'src/input.css', '--minify'], {
  cwd: __dirname,
  encoding: 'utf-8',
})

// Substitute the version placeholder and inject the compiled CSS into the static template.
const template = readFileSync(resolve(__dirname, 'src/template.html'), 'utf-8')
const fullDocument = template
  .replaceAll('__VERSION__', version)
  .replace('<!--HOMEPAGE_CSS-->', `<style>\n${css}\n</style>`)

writeFileSync(outputPath, fullDocument)
console.log(`✓ Standalone HTML document written to ${outputPath}`)
