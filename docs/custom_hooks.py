import sys
import os
from pathlib import Path

fragment_path = "./docs/overrides/homepage-content.html"
def on_pre_build(config):
    if "serve" in sys.argv:
        Path("./docs/overrides").mkdir(parents=True, exist_ok=True)
        if not os.path.isfile(fragment_path):
            with open(fragment_path, "w") as text_file:
                print(f"""
                This is a debug build, to see the homepage here first run
                `npm run build:fragment` in the homepage project directory.

                Click <a target="_top" href="/endpoints">here</a> to go to main docs
                 """, file=text_file)
    if "build" in sys.argv:
        if not os.path.isfile(fragment_path):
            raise Exception("Homepage build files not found, have you built the homepage?")
