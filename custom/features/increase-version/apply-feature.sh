echo "**************************"
echo "**** FEATURE [increase-version] ****"
echo "**************************"

#
# MAJOR.minor.PATCH-LABEL
#
# Check semver.org
#

increase=$1
if ["$increase" == ""]; then
    increase="PATCH"
fi;

#sed -n "s/.*\(\"[0-9].[0-9].[0-9]\).*/\1/p" < src/main/webapp/app/app.constants.js
version="$(sed -n "s/.*\"\([0-9]*.[0-9]*.[0-9]*-[A-Z]*\).*/\1/p" < src/main/webapp/app/app.constants.js)"

v1="$(echo $version|awk -F'.' '{print $1}')"
v2="$(echo $version|awk -F'.' '{print $2}')"
v3="$(echo $version|awk -F'.' '{print $3}')"
v3a="$(echo $v3|awk -F'-' '{print $1}')"
v3b="$(echo $v3|awk -F'-' '{print $2}')"

v3=v3a

if [ "$increase" = "MAJOR" ]; then
  echo "INCREATE MAJOR"
  v1=$((v1 + 1))
fi;
if [ "$increase" = "MINOR" ]; then
    echo "INCREATE MINOR"
    v2=$((v2 + 1))
fi;
if [ "$increase" = "PATCH" ]; then
    echo "INCREATE PATCH"
    v3=$((v3 + 1))
fi;

v3X=""
if [ "$v3b" = "" ]; then
    v3X=""
else
    v3X="-"$v3b
fi;

newVersion=$v1"."$v2"."$v3$v3X
echo "COMMAND: $increase"
echo "OLD: $version | NEW: $newVersion"
#
# Code updates
#

sed -i "s|version = '$version'|version = '$newVersion'|" build.gradle
sed -i "s|<version>$version</version>|<version>$newVersion</version>|" pom.xml
sed -i "s|$version|$newVersion|" src/main/webapp/app/app.constants.js

cat src/main/webapp/app/app.constants.js
