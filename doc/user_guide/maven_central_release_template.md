# Template of `maven_central_release.yml`

Create a `/.github/workflows/maven_central_release.yml` file in your project with the following content:

```
name: Maven Central Release

on:
  workflow_dispatch:

jobs:
  publish:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Set up Maven Central Repository
        uses: actions/setup-java@v1
        with:
          java-version: 11
          server-id: ossrh
          server-username: MAVEN_USERNAME
          server-password: MAVEN_PASSWORD
      - name: Import GPG Key
        run:
          gpg --import --batch <(echo "${{ secrets.OSSRH_GPG_SECRET_KEY }}")
      - name: Publish to Central Repository
        env:
          MAVEN_USERNAME: ${{ secrets.OSSRH_USERNAME }}
          MAVEN_PASSWORD: ${{ secrets.OSSRH_PASSWORD }}
        run: mvn clean -Dgpg.skip=false -Dgpg.passphrase=${{ secrets.OSSRH_GPG_SECRET_KEY_PASSWORD }} deploy
```