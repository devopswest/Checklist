appname=$1

echo "Pushing $appname"

tag=$2
if [ "$tag" = "" ]; then
  tag="test"
fi;

registry="$(echo 'dtr.pwcinternal.com'|awk '{print tolower($0)}')"

echo "Pushing image to repo "$appname" $tag a to $registry"

docker images|grep $appname|head -1
imageId="$(docker images|grep $appname|head -1|awk '{print $3}')"
version="$(docker images|grep pwcnow/$appname|head -1|awk '{print $2}')"

v1="$(echo $version|awk -F'.' '{print $1}')"
v2="$(echo $version|awk -F'.' '{print $2}')"
v3="$(echo $version|awk -F'.' '{print $3}')"

v1="$(echo $v1|awk '{print substr($0,2)}')"
v3="$(echo $v3|awk -F'-' '{print $1}')"


echo "Image Id $imageId   $v1  $v2   $v3"
docker kill pwcnow-$appname
docker rmi $registry/pwcnow/authz-$appname:$tag
docker tag $imageId $registry/pwcnow/authz-$appname:$tag
docker push  $registry/pwcnow/authz-$appname:$tag

if [ "$tag" = "stage" ]; then
  echo "Pushing Versioned Tags [$v1, $v2, $v3]"
  docker rmi $imageId $registry/pwcnow/authz-$appname:$v1
  docker rmi $imageId $registry/pwcnow/authz-$appname:$v1.$v2
  docker rmi $imageId $registry/pwcnow/authz-$appname:$v1.$v2.$v3
  
  docker tag $imageId $registry/pwcnow/authz-$appname:$v1
  docker tag $imageId $registry/pwcnow/authz-$appname:$v1.$v2
  docker tag $imageId $registry/pwcnow/authz-$appname:$v1.$v2.$v3
  
  docker push  $registry/pwcnow/authz-$appname:$v1
  docker push  $registry/pwcnow/authz-$appname:$v1.$v2
  docker push  $registry/pwcnow/authz-$appname:$v1.$v2.$v3
fi;

echo "Images Pushed...."

docker images|grep $appname