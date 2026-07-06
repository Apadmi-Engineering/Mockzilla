# Third-Party Licenses

This document lists the third-party libraries and tools used by Mockzilla, along with their licenses.

Versions are intentionally omitted — this file covers all versions of each dependency used in the project. The file should be reviewed whenever a dependency changes its major version.

---

## 1. Kotlin / KMP Library

Dependencies bundled into the distributed Android AAR, iOS XCFramework, JVM jar, and JavaScript output.

| Package | License |
|---|---|
| [Ktor](https://ktor.io) | [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0) |
| [kotlinx-coroutines](https://github.com/Kotlin/kotlinx.coroutines) | [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0) |
| [kotlinx-serialization](https://github.com/Kotlin/kotlinx.serialization) | [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0) |
| [kotlinx-datetime](https://github.com/Kotlin/kotlinx-datetime) | [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0) |
| [kotlinx-browser](https://github.com/Kotlin/kotlinx-browser) | [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0) |
| [Kermit](https://github.com/touchlab/Kermit) | [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0) |
| [JmDNS](https://github.com/jmdns/jmdns) | [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0) |
| [semver](https://github.com/z4kn4fein/kotlin-semver) | [MIT](https://opensource.org/licenses/MIT) |
| [Mock Service Worker (msw)](https://mswjs.io) | [MIT](https://opensource.org/licenses/MIT) |

---

## 2. Management Desktop App

Additional dependencies compiled into the Compose Multiplatform desktop application and Android management UI, beyond those listed in section 1.

| Package | License |
|---|---|
| [Koin](https://insert-koin.io) | [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0) |
| [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/) | [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0) |
| [Material Icons](https://github.com/androidx/androidx) | [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0) |
| [Material 3](https://m3.material.io) | [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0) |
| [AndroidX Activity](https://developer.android.com/jetpack/androidx/releases/activity) | [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0) |
| [AndroidX Lifecycle](https://developer.android.com/jetpack/androidx/releases/lifecycle) | [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0) |
| [Jetpack Navigation](https://developer.android.com/guide/navigation) | [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0) |
| [Google Play Services — Ads Identifier](https://developers.google.com/android/guides/setup) | [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0) |
| [Adam](https://github.com/Malinskiy/adam) | [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0) |
| [Lyricist](https://github.com/adrielcafe/lyricist) | [MIT](https://opensource.org/licenses/MIT) |

---

## 3. Kotlin Build & Tooling

Gradle plugins, code generation tools, linters, and test libraries. These are used during the build process only and are not distributed to end users.

| Package | License |
|---|---|
| [Kotlin](https://kotlinlang.org) | [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0) |
| [Android Gradle Plugin](https://developer.android.com/build) | [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0) |
| [KSP — Kotlin Symbol Processing](https://github.com/google/ksp) | [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0) |
| [BuildKonfig](https://github.com/yshrsmz/BuildKonfig) | [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0) |
| [Diktat (via KTlint)](https://pinterest.github.io/ktlint/) | [MIT](https://opensource.org/licenses/MIT) |
| [Spotless](https://github.com/diffplug/spotless) | [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0) |
| [Dokka](https://github.com/Kotlin/dokka) | [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0) |
| [Kover](https://github.com/Kotlin/kotlinx-kover) | [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0) |
| [Gradle Maven Publish Plugin](https://github.com/vanniktech/gradle-maven-publish-plugin) | [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0) |
| [MockK](https://mockk.io) | [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0) |
| [Turbine](https://github.com/cashapp/turbine) | [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0) |
| [Test Parameter Injector](https://github.com/google/TestParameterInjector) | [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0) |
| [AndroidX Test](https://developer.android.com/jetpack/androidx/releases/test) | [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0) |
| [Conveyor](https://github.com/hydraulic-software/conveyor) | [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0) |

---

## 4. Flutter / Dart Plugin

Runtime dependencies compiled into the distributed Flutter plugin and consumed by Flutter app developers.

| Package | License |
|---|---|
| [plugin_platform_interface](https://pub.dev/packages/plugin_platform_interface) | [BSD-3-Clause](https://opensource.org/licenses/BSD-3-Clause) |
| [freezed_annotation](https://pub.dev/packages/freezed_annotation) | [MIT](https://opensource.org/licenses/MIT) |
| [meta](https://pub.dev/packages/meta) | [BSD-3-Clause](https://opensource.org/licenses/BSD-3-Clause) |
| [web](https://pub.dev/packages/web) | [BSD-3-Clause](https://opensource.org/licenses/BSD-3-Clause) |
| [json_annotation](https://pub.dev/packages/json_annotation) | [BSD-3-Clause](https://opensource.org/licenses/BSD-3-Clause) |
| [Dio](https://pub.dev/packages/dio) | [MIT](https://opensource.org/licenses/MIT) |
| [retrofit](https://pub.dev/packages/retrofit) | [MIT](https://opensource.org/licenses/MIT) |
| [logger](https://pub.dev/packages/logger) | [MIT](https://opensource.org/licenses/MIT) |
| [cupertino_icons](https://pub.dev/packages/cupertino_icons) | [MIT](https://opensource.org/licenses/MIT) |

---

## 5. Flutter Dev & Tooling

Code generation, linting, and test packages used during Flutter development only; not distributed to end users.

| Package | License |
|---|---|
| [Melos](https://melos.invertase.dev) | [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0) |
| [build_runner](https://pub.dev/packages/build_runner) | [BSD-3-Clause](https://opensource.org/licenses/BSD-3-Clause) |
| [freezed](https://pub.dev/packages/freezed) | [MIT](https://opensource.org/licenses/MIT) |
| [json_serializable](https://pub.dev/packages/json_serializable) | [BSD-3-Clause](https://opensource.org/licenses/BSD-3-Clause) |
| [mockito](https://pub.dev/packages/mockito) | [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0) |
| [Pigeon](https://pub.dev/packages/pigeon) | [BSD-3-Clause](https://opensource.org/licenses/BSD-3-Clause) |
| [flutter_lints](https://pub.dev/packages/flutter_lints) | [BSD-3-Clause](https://opensource.org/licenses/BSD-3-Clause) |
| [retrofit_generator](https://pub.dev/packages/retrofit_generator) | [MIT](https://opensource.org/licenses/MIT) |

---

## 6. Web Homepage

Dependencies bundled into the compiled `docs/homepage` website.

| Package | License |
|---|---|
| [React](https://react.dev) | [MIT](https://opensource.org/licenses/MIT) |
| [React DOM](https://react.dev) | [MIT](https://opensource.org/licenses/MIT) |
| [react-syntax-highlighter](https://github.com/react-syntax-highlighter/react-syntax-highlighter) | [MIT](https://opensource.org/licenses/MIT) |
| [class-variance-authority](https://cva.style/docs) | [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0) |
| [clsx](https://github.com/lukeed/clsx) | [MIT](https://opensource.org/licenses/MIT) |
| [lucide-react](https://lucide.dev) | [ISC](https://opensource.org/licenses/ISC) |
| [tailwind-merge](https://github.com/dcastil/tailwind-merge) | [MIT](https://opensource.org/licenses/MIT) |
| [Tailwind CSS](https://tailwindcss.com) | [MIT](https://opensource.org/licenses/MIT) |
| [shadcn/ui](https://ui.shadcn.com) | [MIT](https://opensource.org/licenses/MIT) |

---

## 7. Web Dev Tooling

Build, type-checking, and linting tools for the homepage. Not distributed to end users.

| Package | License |
|---|---|
| [Vite](https://vitejs.dev) | [MIT](https://opensource.org/licenses/MIT) |
| [TypeScript](https://www.typescriptlang.org) | [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0) |
| [typescript-eslint](https://typescript-eslint.io) | [MIT](https://opensource.org/licenses/MIT) |
| [ESLint](https://eslint.org) | [MIT](https://opensource.org/licenses/MIT) |
| [eslint-plugin-react-hooks](https://www.npmjs.com/package/eslint-plugin-react-hooks) | [MIT](https://opensource.org/licenses/MIT) |
| [eslint-plugin-react-refresh](https://github.com/ArnaudBarre/eslint-plugin-react-refresh) | [MIT](https://opensource.org/licenses/MIT) |
| [@eslint/js](https://eslint.org) | [MIT](https://opensource.org/licenses/MIT) |
| [globals](https://github.com/sindresorhus/globals) | [MIT](https://opensource.org/licenses/MIT) |
| [@vitejs/plugin-react](https://github.com/vitejs/vite-plugin-react) | [MIT](https://opensource.org/licenses/MIT) |
| [@vitejs/plugin-react-swc](https://github.com/vitejs/vite-plugin-react-swc) | [MIT](https://opensource.org/licenses/MIT) |
| [@tailwindcss/vite](https://tailwindcss.com) | [MIT](https://opensource.org/licenses/MIT) |
| [babel-plugin-react-compiler](https://react.dev/learn/react-compiler) | [MIT](https://opensource.org/licenses/MIT) |
| [@types/react](https://github.com/DefinitelyTyped/DefinitelyTyped) | [MIT](https://opensource.org/licenses/MIT) |
| [@types/react-dom](https://github.com/DefinitelyTyped/DefinitelyTyped) | [MIT](https://opensource.org/licenses/MIT) |
| [@types/react-syntax-highlighter](https://github.com/DefinitelyTyped/DefinitelyTyped) | [MIT](https://opensource.org/licenses/MIT) |

---

## 8. CI / Release Tooling (Ruby / Fastlane)

Used in the CI/CD pipeline only. Not distributed to end users. Only direct Gemfile dependencies are listed; transitive dependencies are recorded in `Gemfile.lock`.

| Package | License |
|---|---|
| [fastlane](https://fastlane.tools) | [MIT](https://opensource.org/licenses/MIT) |
| [fastlane-plugin-flutter](https://github.com/Flutter-Fastlane-Plugins/fastlane-plugin-flutter) | [MIT](https://opensource.org/licenses/MIT) |
| [fastlane-plugin-screenshotbot](https://screenshotbot.io) | [MIT](https://opensource.org/licenses/MIT) |
| [fastlane-plugin-apadmi_grout](https://github.com/Apadmi-Engineering/grout) | [MIT](https://opensource.org/licenses/MIT) |
| [CocoaPods](https://cocoapods.org) | [MIT](https://opensource.org/licenses/MIT) |

---

## 9. Docs Tooling (Python) 

Used to build the documentation site only. Not distributed to end users.

| Package | License |
|---|---|
| [zensical](https://github.com/zensical/zensical) | [MIT](https://opensource.org/licenses/MIT) |
