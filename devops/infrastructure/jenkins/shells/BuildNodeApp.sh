appname="$1"
echo "Building $appname"

version=$(cat package.json \
  | grep version \
  | head -1 \
  | awk -F: '{ print $2 }' \
  | sed 's/[",]//g' \
  | tr -d '[[:space:]]')

build="$BUILD_SEQUENCE"

echo "Processing version $appname $version BUILD: $build"

docker build --tag="pwcnow/$appname:$version"  .
imageId="$(docker images|grep $appname|head -1|awk '{print $3}')"

echo "BUILD: $appname $version Image Id $imageId"
docker images|grep $appname



