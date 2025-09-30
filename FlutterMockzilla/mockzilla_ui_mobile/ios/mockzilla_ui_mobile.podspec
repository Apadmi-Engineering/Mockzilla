#
# To learn more about a Podspec see http://guides.cocoapods.org/syntax/podspec.html.
# Run `pod lib lint mockzilla_mobile_ui.podspec` to validate before publishing.
#
Pod::Spec.new do |s|
  s.name             = 'mockzilla_ui_mobile'
  # x-release-please-start-version
  s.version          = '0.0.1'
  # x-release-please-end
  s.summary          = 'The iOS implementation for the mockzilla mobile ui plugin.'
  s.description      = <<-DESC
The iOS implementation for the mockzilla mobile ui plugin.
                       DESC
  s.homepage         = 'https://apadmi-engineering.github.io/Mockzilla/'
  s.license          = { :file => '../LICENSE' }
  s.author           = { 'Apadmi Ltd.' => 'samdc@apadmi.com' }
  s.source           = { :path => '.' }
  s.source_files = 'mockzilla_ui_mobile/Sources/mockzilla_ui_mobile/**/*.swift'
  s.dependency 'Flutter'
  s.platform = :ios, '13.0'

  # Flutter.framework does not contain a i386 slice.
  s.pod_target_xcconfig = { 'DEFINES_MODULE' => 'YES', 'EXCLUDED_ARCHS[sdk=iphonesimulator*]' => 'i386' }
  s.swift_version = '5.0'

   # Since this Flutter wrapper is so thin it's deployed along with each Kotlin update so
   # they share versions
  # x-release-please-start-version
  s.dependency 'SwiftMockzillaMobileUi', '0.0.4'
  # x-release-please-end
end
