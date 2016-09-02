
mkdir ./custom/features/$1

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
