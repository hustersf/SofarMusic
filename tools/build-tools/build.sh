#!/bin/bash
export ANDROID_HOME=/Users/zhongchaohan/workspace/kwai-android/libs/sdk
export GRADLE_HOME=/Users/zhongchaohan/workspace/kwai-android/libs/gradle-2.2.1
export GRADLE_OPTS="-Xms128m -Xmx1024m"
export PATH=/usr/local/bin:${PATH}:${GRADLE_HOME}/bin:${ANDROID_NDK_HOME}:.
cd `dirname $0`
WORK_PATH="`pwd`/gifshow-android"
day=`date +'%Y%m%d'`
export GRADLE_OPTS="-Xms128m -Xmx1024m"

RELEASE_BUILD_APK_OUPUT="${WORK_PATH}/build/outputs"
VERSION_CFG_FILE="./build_version_cfg"
GRADLE_FILE="${WORK_PATH}/build.gradle"

NEW_VERSION_NAME=''
NEW_VERSION_CODE=''

while getopts b:n:c:A opt; do
    case $opt in
        b) branch="$OPTARG"
            ;;
        n) version_name="$OPTARG"
            ;;
        c) version_code="$OPTARG"
            ;;
        A) all_channels="yes"
            ;;
    esac
done

function gradle_assR() {
   rm -rf ${RELEASE_BUILD_APK_OUPUT}
   gradle clean
   if [ -z "${all_channels}" ];then
       gradle assembleGifmakerRelease --info --stacktrace
   else
       gradle assembleRelease --info --stacktrace
   fi
}

function git_reset_to_branch() {
    local branch_name=$1
    git reset --hard HEAD
    git checkout ${branch_name}
    git rebase origin/${branch_name}
}

function git_create_version_branch() {
    local branch_name=$1
    git reset --hard
    git checkout -b ${branch_name} master
    git push origin ${branch_name}
    git fetch
}

function git_checkout_version_branch() {
    local branch_name=$1
    git reset --hard
    EXIST="`git branch | grep ${branch_name}`"

    if [ -z "${EXIST}" ];then
        git checkout -b ${branch_name} origin/${branch_name}
    else
        git checkout ${branch_name}
        git rebase origin/${branch_name}
    fi
}

################################################################################
# get latest version code and version name from ${VERSION_CFG_FILE}
# Globals:
#   NEW_VERSION_NAME
#   NEW_VERSION_CODE
# Arguments:
#   version config git root directory
#   version config file
# Returns:
#   None
################################################################################
function get_latest_version() {
    #####################################################
    #   get latest version name & code
    #####################################################
    local latestVersionName=`cat ${VERSION_CFG_FILE} | cut -d '.' -f1-3`
    local latestVersionCode=`cat ${VERSION_CFG_FILE} | cut -d '.' -f4`
    echo "latest version ----------- "${latestVersionName}.${latestVersionCode}
    if [ -z "${version_name}" ];then
      NEW_VERSION_NAME=${latestVersionName}
    else
      NEW_VERSION_NAME=${version_name}
    fi
    if [ -z "${version_code}" ];then
      let NEW_VERSION_CODE=${latestVersionCode}+1
    else
      NEW_VERSION_CODE=${version_code}
    fi
    echo "new version ----------- "${NEW_VERSION_NAME}.${NEW_VERSION_CODE}
}

function update_version_cfg() {
  if [ -z "${version_code}" ];then
    echo ${NEW_VERSION_NAME}.${NEW_VERSION_CODE} > ${VERSION_CFG_FILE}
  fi
}
################################################################################
# update version code and version name in AndroidManifest.xml
# if channel value is master, update both version name and version code
# if channle value is NOT master, ie. release/RB_4.4, update only version code
# Globals:
#   channel
# Arguments:
#   version name, i.e 4.0.0
#   version code, i.e 5555
# Returns:
#   None
################################################################################
function update_version_in_gradle() {
    sed -i "" "s/\(versionCode \)\([0-9]*\)/\1${NEW_VERSION_CODE}/" ${GRADLE_FILE}
    sed -i "" "s/\(versionName \"\)\([0-9\.]*\)/\1${NEW_VERSION_NAME}\.${NEW_VERSION_CODE}/" ${GRADLE_FILE}
}

get_latest_version
cd "${WORK_PATH}"
git reset --hard HEAD
git fetch
git_reset_to_branch 'master'


if [ "${branch}" = "master" ];then
  update_version_in_gradle
  gradle_assR
else
  HAS_BRANCH="`git branch -r | grep ${NEW_VERSION_NAME}`"
  if [ -z "${HAS_BRANCH}" ];then
      git_create_version_branch release/RB_${NEW_VERSION_NAME}
  else
      git_checkout_version_branch release/RB_${NEW_VERSION_NAME}
  fi

  update_version_in_gradle
  gradle_assR

  # Reset and checkout master
  git_reset_to_branch 'master'
fi

#################################################################################
##               Copy files to server
#################################################################################

APK_DAILY_BUILD_PATH="/usr/local/opt/nginx/html/build/gifshow-android/${day}/${NEW_VERSION_NAME}.${NEW_VERSION_CODE}"
if [ ! -d "${RELEASE_BUILD_APK_OUPUT}/apk" ];then
  echo "############ Build Fail ##############"
  exit 1
fi
#####################################################
#   update version name & code to global cfg file
#####################################################
cd ..
update_version_cfg
mkdir -p ${APK_DAILY_BUILD_PATH}/apk
mkdir -p ${APK_DAILY_BUILD_PATH}/mapping
mv ${RELEASE_BUILD_APK_OUPUT}/apk/kuaishou_*.apk ${APK_DAILY_BUILD_PATH}/apk/
mv ${RELEASE_BUILD_APK_OUPUT}/apk/kwai_*.apk ${APK_DAILY_BUILD_PATH}/apk/
mv ${RELEASE_BUILD_APK_OUPUT}/mapping/gifmaker/release/mapping.txt ${APK_DAILY_BUILD_PATH}/mapping/mapping.txt
rm -rf ${RELEASE_BUILD_APK_OUPUT}
