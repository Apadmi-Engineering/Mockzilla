lane :generate_docs do
    output_dir = "#{lane_context[:repo_root]}/generated_docs"

    # Build mkdocs
    sh("cd #{lane_context[:repo_root]}/docs; mkdocs build -d #{output_dir}")

    # Generate Kotlin documentation
    gradle(
        tasks: ["dokkaHtml"],
        system_properties: {
            "docsOutputDirectory" => "#{output_dir}/dokka"
        }
    )
end