This documentation is all built using [MkDocs](https://www.mkdocs.org/)
with the [Material theme](https://squidfunk.github.io/mkdocs-material/).

**Their documentation is brilliant so please check their docs if this is not sufficient.**

Please ensure this documentation is updated whenever changes are made to
the source code / configuration.

!!! note
    The source for these docs lives in the `docs` directory within the repo.

    All the following commands assume you've `cd`'d into the `docs` directory.

## Installing Dependencies

Ensure Python is installed on your system.

Tested on python `v{{get_python_version()}}`

Python: 
```bash
# Install all dependencies
pip install -r requirements.txt
```

Python 3:
```bash
# Install all dependencies
pip3 install -r requirements.txt
```

Run the following to start the server.

This supports hot reloading so updating the docs should
automatically reload the docs in your browser.

```bash
mkdocs serve
```

## Macros

The docs also uses the [mkdocs-macros](https://mkdocs-macros-plugin.readthedocs.io/en/latest/) plugin.
This lets us call out to python code (and a load of other features) from within markdown.

See the `main.py` file which includes some useful macros.

This is all quite self explanatory when you look at the code.

## Building the whole site

Run the following:

`bundle exec fastlane generate_docs`

This will generate the [dokka](https://github.com/Kotlin/dokka) documentation and put the website together.