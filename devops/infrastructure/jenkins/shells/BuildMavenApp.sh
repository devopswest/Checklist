appname="$(echo $POM_DISPLAYNAME|awk '{print substr($0,7)}')"
echo "Building $appname"

version="$(ls ./$1/target/pwcnow$appname*.war|awk '{split($appname,array,"-")} END{print "v" array[2] "-" array[3]}')"
build="$(ls ./$1/target/pwcnow$appname*.war|awk '{split($appname,array,"-")} {split(array[4],array2,".")} END{print array2[1]}')"
echo "Processing version $appname $version BUILD: $build"

docker build --tag="pwcnow/$appname:$version"  .
imageId="$(docker images|grep $appname|head -1|awk '{print $3}')"

echo "BUILD: $appname $version Image Id $imageId"
docker images|grep $appname