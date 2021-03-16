# Templates of `print_quick_checksum.yml`

Copy the content into a file `/.github/workflows/print_quick_checksum.yml` in your project.

## For Java Maven Project

See [an example from this project](../../../.github/workflows/print_quick_checksum.yml).

## For Scala Sbt Project

```
name: Print Quick Checksum

on:
  workflow_dispatch:

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - name: Checkout the repository
        uses: actions/checkout@v2
      - name: Setup Scala
        uses: olafurpg/setup-scala@v10
        with:
          java-version: adopt@1.11
      - name: Assembly with SBT skipping tests
        run: sbt assembly
      - name: Prepare checksum
        run: echo 'checksum_start==';find target/scala*/stripped -name *.jar -exec sha256sum "{}" + | xargs;echo '==checksum_end'
```