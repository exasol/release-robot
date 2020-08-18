# Example of `upload_release_asset.yml`

Here you can find examples of `upload_release_asset.yml` file that we you need for automation of the GitHub build and release.  

## For Java Repository
 
```
name: Upload Release Asset

on:
 workflow_dispatch:
    inputs:
      version:
        description: 'Release version'
        required: true
      upload_url:
        description: 'Upload URL'
        required: true

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v2
    - name: Set up JDK 11
      uses: actions/setup-java@v1
      with:
        java-version: 11
    - name: Build with Maven
      run: mvn -B clean package --file pom.xml
 
    - name: Upload Release Asset
      uses: actions/upload-release-asset@v1
      env:
        GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
      with:
        upload_url: ${{ github.event.inputs.upload_url }}
        asset_path: ./target/testing-release-robot-${{ github.event.inputs.version }}.jar
        asset_name: testing-release-robot-${{ github.event.inputs.version }}.jar
        asset_content_type: application/java-archive
```