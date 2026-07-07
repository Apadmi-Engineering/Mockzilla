require 'aws-sdk-s3'
require 'fileutils'

R2_ENDPOINT = 'https://dcc7c62ca2d63963ac7362cc2000fb3f.r2.cloudflarestorage.com'

# Builds an S3 client configured for Cloudflare R2 (S3-compatible API).
def r2_client
  access_key_id = ENV['CLOUDFLARE_ACCESS_KEY_ID']
  secret_access_key = ENV['CLOUDFLARE_SECRET_ACCESS_KEY']

  if access_key_id.nil? || secret_access_key.nil?
    raise "Error: CLOUDFLARE_ACCESS_KEY_ID and CLOUDFLARE_SECRET_ACCESS_KEY must be set"
  end

  Aws::S3::Client.new(
    region: 'auto',
    endpoint: R2_ENDPOINT,
    access_key_id: access_key_id,
    secret_access_key: secret_access_key,
  )
end

# Uploads a local file to an R2 bucket.
#
# @param bucket [String] the R2 bucket name (e.g. 'mockzilla-js-artifacts')
# @param key [String] the destination object key (e.g. 'mockzilla-v3.2.1/javascript_output.zip')
# @param file_path [String] absolute path to the local file to upload
def upload_file_to_r2(bucket:, key:, file_path:)

  unless File.exist?(file_path)
    raise "Error: file not found at path: #{file_path}"
  end

  puts "Uploading #{file_path} to r2://#{bucket}/#{key}..."
  Aws::S3::Resource.new(client: r2_client).bucket(bucket).object(key).upload_file(file_path)
  puts "Upload complete."
end

# Downloads an object from R2 and unzips it into a directory, then deletes
# the downloaded zip.
#
# @param bucket [String] the R2 bucket name
# @param key [String] the object key of the zip to download
# @param unzip_dir [String] the directory where contents will be extracted
def download_and_unzip_r2_object(bucket:, key:, unzip_dir:)
  download_filename = File.basename(key)
  puts "Downloading r2://#{bucket}/#{key} to #{download_filename}..."

  begin
    Aws::S3::Resource.new(client: r2_client).bucket(bucket).object(key).download_file(download_filename)
  rescue Aws::S3::Errors::ServiceError => e
    puts "Download failed: #{e.message}"
    raise e
  end

  puts "Download complete: #{download_filename}"
  FileUtils.mkdir_p(unzip_dir)

  unzip_result = system("unzip -j -o #{download_filename} -d #{unzip_dir}")
  unless unzip_result
    raise "Error: 'unzip' command failed. Check if 'unzip' is installed on your system."
  end

  File.delete(download_filename)
  puts "Unzipping complete. Original zip file deleted."
end
