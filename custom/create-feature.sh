
mkdir ./custom/features/$1
cp -r ./src custom/features/$1

touch ./custom/features/$1/apply-feature.sh

echo "echo \"**************************\"" >> ./custom/features/$1/apply-feature.sh
echo "echo \"**** FEATURE [$1] ****\"" >> ./custom/features/$1/apply-feature.sh
echo "echo \"**************************\"" >> ./custom/features/$1/apply-feature.sh

touch ./custom/features/$1/README.md

echo "" >> ./custom/features/$1/apply-feature.sh
echo "" >> ./custom/features/$1/apply-feature.sh
echo "" >> ./custom/features/$1/apply-feature.sh

chmod +x ./custom/features/$1/apply-feature.sh

echo "#" >> ./custom/features/$1/apply-feature.sh
echo "# Code updates" >> ./custom/features/$1/apply-feature.sh
echo "#" >> ./custom/features/$1/apply-feature.sh
echo "cp -r custom/features/$1/src ./" >> ./custom/features/$1/apply-feature.sh

sed -i "s|{\"id\":\"NEW\"}| \
{\n \
        \"id\" : \"$1\",\n \
        \"name\":\"$1\",\n \
        \"description\": \"$1\",\n \
        \"path\": \"$1\",\n \
        \"scripts\": {\n \
            \"apply\": \"apply-feature.sh\",\n \
            \"rollback\": \"rollback-feature.sh\"\n \
        },\n \
        \"dependencies\": [\n \
        ],\n \
        \"active\" : \"true\"\n \
    },\n \
    {\"id\":\"NEW\"}\n|" custom/features.json

