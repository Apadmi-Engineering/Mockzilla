lane :generate_docs do
    output_dir = "#{lane_context[:repo_root]}/generated_docs"

    # Build the page to redirect to the desktop app download site
    sh("cd #{lane_context[:repo_root]}/docs; python -c 'import main; main.update_download_file()'")

    # Build the homepage
    sh("
        cd #{lane_context[:repo_root]}/docs/homepage;
        npm i;
        export VITE_VERSION_NAME=#{get_core_mockzilla_version_name};
        npm run build;
    ");

    # Generate Kotlin documentation
    gradle(
        tasks: [":dokkaHtmlMultiModule"],
        system_properties: {
            "docsOutputDirectory" => "#{lane_context[:repo_root]}/docs/docs/dokka"
        }
    )

    # Build mkdocs
    sh("cd #{lane_context[:repo_root]}/docs; mkdocs build -d #{output_dir}")
end
