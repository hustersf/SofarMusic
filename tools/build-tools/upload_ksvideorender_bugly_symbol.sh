#!/usr/bin/env bash

repo=git@idc.git.corp.kuaishou.com:versions/KSVideoRenderSDK_BuglySymbol.git
tmpdir=KSVideoRenderSDK_BuglySymbol_Temp
symbolFile=android/ksvideorendersdk/build/intermediates/cmake/release/obj/armeabi-v7a/buglySymbol*.zip

bugly_app_key="9e86d60c-20d9-4b6f-8ad8-2a4cb5bd816f"
bugly_app_id="98387f1866"

version=`cat Dependencies.kt| egrep 'com.kwai.video:ksvideorendersdk:(\S+)@aar' | sed 's/.*://' | sed 's/@.*//'`
echo "symbol version: $version"

version_name=`cat gradle.properties | egrep '^VERSION_NAME=(\S+)' | sed 's/.*=//'`
version_code=`cat gradle.properties | egrep '^VERSION_CODE=(\S+)' | sed 's/.*=//'`
app_version="$version_name.$version_code"
echo $app_version


git clone $repo $tmpdir
cd $tmpdir
git fetch origin $version
git checkout origin $version

curl -k "https://api.bugly.qq.com/openapi/file/upload/symbol?app_key=$bugly_app_key&app_id=$bugly_app_id" --form "api_version=1" --form "app_id=$bugly_app_id" --form "app_key=$bugly_app_key" --form "symbolType=3"  --form "bundleId=com.kwai.video" --form "productVersion=$app_version" --form "fileName=symbol.zip" --form "file=@symbol.zip" --verbose

cd ..
rm -rf $tmpdir



