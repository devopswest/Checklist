
category="development"
feature=$1

mkdir ./custom/features//$category/$feature
cp -r ./src custom/features//$category/$feature

touch ./custom/features/$category/$feature/apply-feature.sh

echo "echo \"**************************\"" >> ./custom/features/$category/$feature/apply-feature.sh
echo "echo \"**** FEATURE [$feature] ****\"" >> ./custom/features/$category/$feature/apply-feature.sh
echo "echo \"**************************\"" >> ./custom/features/$category/$feature/apply-feature.sh

touch ./custom/features/$category/$feature/README.md

echo "" >> ./custom/features/$category/$feature/apply-feature.sh
echo "" >> ./custom/features/$category/$feature/apply-feature.sh
echo "" >> ./custom/features/$category/$feature/apply-feature.sh

chmod +x ./custom/features/$category/$feature/apply-feature.sh

echo "#" >> ./custom/features/$category/$feature/apply-feature.sh
echo "# Code updates" >> ./custom/features/$category/$feature/apply-feature.sh
echo "#" >> ./custom/features/$category/$feature/apply-feature.sh
echo "cp -r custom/features/\$category/\$feature/src ./" >> ./custom/features/$category/$feature/apply-feature.sh

sed -i "s|{\"id\":\"NEW\"}| \
{\n \
        \"id\" : \"\$feature\",\n \
        \"name\":\"\$feature\",\n \
        \"description\": \"\$feature\",\n \
        \"path\": \"\$caterory\/\$feature\",\n \
        \"scripts\": {\n \
            \"apply\": \"apply-feature.sh\",\n \
            \"rollback\": \"rollback-feature.sh\"\n \
        },\n \
        \"dependencies\": [\n \
        ],\n \
        \"active\" : \"true\"\n \
    },\n \
    {\"id\":\"NEW\"}\n|" custom/features.json

