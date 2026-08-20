lane :generate_docs do
    # Build the page to redirect to the desktop app download site
    sh("cd #{lane_context[:repo_root]}/docs; python -c 'import main; main.update_download_file()'")

    # Build the homepage
    sh("
        cd #{lane_context[:repo_root]}/docs/homepage;
        npm i;
        export MOCKZILLA_VERSION_NAME=#{get_core_mockzilla_version_name};
        npm run build:fragment;
    ");

    # Generate Kotlin documentation
    gradle(
        tasks: [":dokkaGeneratePublicationHtml"],
        system_properties: {
            "docsOutputDirectory" => "#{lane_context[:repo_root]}/docs/docs/dokka"
        }
    )

    # Build docs
    sh("cd #{lane_context[:repo_root]}/docs; zensical build")

    # Generate llms.txt / llms-full.txt for consumers using LLM coding assistants.
    # Must run after `zensical build`, which owns the `site` output directory.
    sh("cd #{lane_context[:repo_root]}/docs; python -c 'import main; main.generate_llms_files()'")
end
