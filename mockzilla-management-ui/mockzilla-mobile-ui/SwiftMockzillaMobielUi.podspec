Pod::Spec.new do |spec|
    spec.name                     = 'SwiftMockzillaMobielUi'
    spec.version                  = '0.0.1'
    spec.homepage                 = 'https://apadmi-engineering.github.io/Mockzilla/'
    spec.source                   = { :http=> ''}
    spec.authors                  = ''
    spec.license                  = {:type => 'MIT', :file => 'LICENSE'}
    spec.summary                  = 'A solution for running and configuring a local HTTP server to mimic REST API endpoints used by your application.'
                
    spec.libraries                = 'c++'
    spec.ios.deployment_target    = '13.0'
                
                
                
    spec.xcconfig = {
        'ENABLE_USER_SCRIPT_SANDBOXING' => 'NO',
    }
                
    spec.pod_target_xcconfig = {
        'KOTLIN_PROJECT_PATH' => ':mockzilla-management-ui:mockzilla-mobile-ui',
        'PRODUCT_MODULE_NAME' => 'mockzilla_mobile_ui',
    }
                
    spec.script_phases = [
        {
            :name => 'Build SwiftMockzillaMobielUi',
            :execution_position => :before_compile,
            :shell_path => '/bin/sh',
            :script => <<-SCRIPT
                if [ "YES" = "$OVERRIDE_KOTLIN_BUILD_IDE_SUPPORTED" ]; then
                  echo "Skipping Gradle build task invocation due to OVERRIDE_KOTLIN_BUILD_IDE_SUPPORTED environment variable set to \"YES\""
                  exit 0
                fi
                set -ev
                REPO_ROOT="$PODS_TARGET_SRCROOT"
                "$REPO_ROOT/../../gradlew" -p "$REPO_ROOT" $KOTLIN_PROJECT_PATH:syncFramework \
                    -Pkotlin.native.cocoapods.platform=$PLATFORM_NAME \
                    -Pkotlin.native.cocoapods.archs="$ARCHS" \
                    -Pkotlin.native.cocoapods.configuration="$CONFIGURATION"
            SCRIPT
        }
    ]
    spec.vendored_frameworks = 'Mockzilla.xcframework'
    spec.source_files = 'Sources/SwiftMockzilla/SwiftMockzilla.swift'
    spec.swift_version = '5.9.2'
    spec.resources = ['build/compose/cocoapods/compose-resources']
end