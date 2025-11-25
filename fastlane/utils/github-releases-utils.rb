require 'net/http'
require 'uri'
require 'json'
require 'fileutils'

# Function to download and unzip a GitHub Release asset
#
# @param tag [String] The full release tag (e.g., 'mockzilla-v3.0.0')
# @param asset_name [String] The exact name of the asset file (e.g., 'javascript_output.zip')
# @param unzip_dir [String] The directory where the contents will be extracted
def download_and_unzip_release_asset(tag, asset_name, unzip_dir)
  owner = "Apadmi-Engineering"
  repo = "Mockzilla"
  api_url = "https://api.github.com/repos/#{owner}/#{repo}/releases/tags/#{tag}"
  download_filename = asset_name
  github_token = ENV['GITHUB_TOKEN']

  puts "Starting process for asset '#{asset_name}' in tag '#{tag}'..."

  # Fetch the GitHub Release information ---
  uri = URI(api_url)
  http = Net::HTTP.new(uri.host, uri.port)
  http.use_ssl = true
  request = Net::HTTP::Get.new(uri)
  request['Accept'] = 'application/vnd.github+json'
  request['X-GitHub-Api-Version'] = '2022-11-28'
  request['Authorization'] = "Bearer #{github_token}" if github_token

  begin
    response = http.request(request)
  rescue StandardError => e
    puts "Error fetching API data: #{e.message} #{e}"
    raise e
  end

  unless response.code == "200"
    puts "API call failed with status code #{response.code}. Check tag or permissions."
    raise e
  end

  # Parse the result and find the asset download URL ---
  data = JSON.parse(response.body)
  asset = data['assets'].find { |a| a['name'] == asset_name }

  if asset.nil?
    puts "Error: Asset '#{asset_name}' not found in the release."
    raise "No matching asset found matching #{asset_name}"
  end

  asset_api_url = asset['url']
  puts "Asset API URL for download: #{asset_api_url}"

  puts "Downloading file to #{download_filename}..."
  download_headers = {
    'Accept' => 'application/octet-stream',
    'X-GitHub-Api-Version' => '2022-11-28'
  }

  begin
    URI.open(asset_api_url, download_headers) do |file|
      File.open(download_filename, 'wb') do |output_file|
        output_file.write(file.read)
      end
    end
    puts "Download complete: #{download_filename}"
  rescue StandardError => e
    puts "Download failed: #{e.message}"
    raise e
  end

  # Unzip the downloaded file using system command ---
  puts "Unzipping #{download_filename} to #{unzip_dir}/ using system 'unzip'..."

  # Ensure the destination directory exists
  system("mkdir -p #{unzip_dir}")

  # Use 'unzip -o' to overwrite files without prompting
  unzip_command = "unzip -j -o #{download_filename} -d #{unzip_dir}"
  unzip_result = system(unzip_command)

  unless unzip_result
    raise "Error: 'unzip' command failed. Check if 'unzip' is installed on your system. #{unzip_result}"
  end

  # Cleanup the downloaded zip file
  File.delete(download_filename)
  puts "Unzipping complete. Original zip file deleted."

  puts "\n✅ Process finished successfully!"
end
