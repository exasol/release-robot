# Template of `release_droid_release_on_maven_central.yml`

Create a `/.github/workflows/release_droid_release_on_maven_central.yml` file in your project with the following content:

```
name: Release Droid - Release On Maven Central

on:
  workflow_dispatch:

jobs:
  publish:
    runs-on: ubuntu-latest
    steps:
      - name: Checkout the repository
        uses: actions/checkout@v2
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
      - name: Cache local Maven repository
        uses: actions/cache@v2
        with:
          path: ~/.m2/repository
          key: ${{ runner.os }}-maven-${{ hashFiles('**/pom.xml') }}
          restore-keys: |
            ${{ runner.os }}-maven-
      - name: Publish to Central Repository
        env:
          MAVEN_USERNAME: ${{ secrets.OSSRH_USERNAME }}
          MAVEN_PASSWORD: ${{ secrets.OSSRH_PASSWORD }}
        run: mvn clean -Dgpg.skip=false -Dgpg.passphrase=${{ secrets.OSSRH_GPG_SECRET_KEY_PASSWORD }} -DskipTests deploy
```