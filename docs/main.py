import os
import glob
from pathlib import Path
import re
import platform

def define_env(env):
  "Hook function"

  def remove_prefix(text, prefix):
       return text[text.startswith(prefix) and len(prefix):]

  @env.macro
  def print_source_file(filename, indent = ""):
      full_path = glob.glob(f'../**/{filename}', recursive=True)[0]

      return Path(full_path).read_text().replace('\n', '\n' + indent)

  @env.macro
  def get_version():
      build_gradle_text = print_source_file("mockzilla/build.gradle.kts")
      version_pattern = r'version\s*=\s*"(.*?)".*'
      match = re.search(version_pattern, build_gradle_text)
      if match:
          return match.group(1)
      else:
          return None

  @env.macro
  def get_python_version():
      return platform.python_version()