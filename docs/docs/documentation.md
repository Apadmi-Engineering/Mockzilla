This documentation is built using [Zensical](https://zensical.org/), a modern static site
generator by the team behind Material for MkDocs.

**Their documentation is great so check it if this is not sufficient.**


## Working on the HomePage

The homepage is a separate React site which is included in the Zensical site.

In your IDE of choice open `docs/homepage` and treat it as a regular standalone React site.
Install dependencies with `npm install` and run it with `npm run dev`.

Note: Run `npm run build` to get your updates to the homepage reflected in the docs site locally.

## Working on the rest of the documentation

Please ensure this documentation is updated whenever changes are made to
the source code / configuration.

!!! note
    The source for these docs lives in the `docs` directory within the repo.

    All the following commands assume you've `cd`'d into the `docs` directory.

## Installing Dependencies

Ensure Python is installed on your system.

Tested on python `v{{get_python_version()}}`

```bash
# Install all dependencies
pip install -r requirements.txt
cd homepage && npm install
```

Run the following to start the server.

This supports hot reloading so updating the docs should
automatically reload the docs in your browser.

```bash
./serve.sh
```

## Macros

The docs use Zensical's built-in macro support, which is compatible with the `mkdocs-macros-plugin`
API. This lets us call out to Python code from within Markdown.

See the `main.py` file which includes some useful macros.

This is all quite self explanatory when you look at the code.

## Building the whole site

Run the following:

`bundle exec fastlane generate_docs`

This will generate the [dokka](https://github.com/Kotlin/dokka) documentation and put the website together.