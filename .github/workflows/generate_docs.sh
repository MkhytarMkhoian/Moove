#!/bin/bash

# The website is built using MkDocs with the Material theme.
# https://squidfunk.github.io/mkdocs-material/
# It requires Python to run.
# Install the packages with the following command:
# pip install mkdocs mkdocs-material mkdocs-redirects

set -ex

# Generate the API docs
./gradlew dokkaHtmlMultiModule
#mv ./build/dokka/api android-docs/docs

# Build the site locally
cd ./android-docs
python3 -m venv venv
source venv/bin/activate
pip3 install mkdocs-material
mkdocs build
