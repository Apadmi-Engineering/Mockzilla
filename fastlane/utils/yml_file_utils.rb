require 'yaml'
require 'fileutils'
require 'open-uri'

def extract_pubspec_property(pubspec_path, property_name)
  unless File.exist?(pubspec_path)
    raise "Error: pubspec file not found at path: #{pubspec_path}"
  end

  pubspec_content = YAML.load_file(pubspec_path)
  prop = pubspec_content[property_name]

  if prop.nil?
    raise "Error: Custom attribute '#{prop}' not found in #{pubspec_path}"
  end

  puts "Extracted property #{property_name}: #{prop}"
  return prop.to_s
end