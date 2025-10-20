import sys
import os
from pathlib import Path

index_dir = "./homepage/build/index.html"
def on_pre_build(config):
    if "serve" in sys.argv:
        Path("./homepage/build/Mockzilla/homepage-assets").mkdir(parents=True, exist_ok=True)
        if not os.path.isfile(index_dir):
            with open("./homepage/build/index.html", "w") as text_file:
                print(f"""
                This is a debug build, to see the homepage here first run
                `npm run build` in the homepage project directory.

                Click <a target="_top" href="/endpoints">here</a> to go to main docs
                 """, file=text_file)
    if "build" in sys.argv:
        if not os.path.isfile(index_dir):
            raise Exception("Homepage build files not found, have you built the homepage?")
