#
# Depends on jq
#
# sudo apt-get install jq.
#
# https://stedolan.github.io/jq/download/
#

# ./features/application-settings/apply-feature.sh
# ./features/css-theme/apply-feature.sh
# ./features/dynamic-menu/apply-feature.sh
# ./features/add-angular-tree/apply-feature.sh
# ./features/load-initial-data/apply-feature.sh
# ./features/checklist-tree/apply-feature.sh
# ./features/audit-profile-tree/apply-feature.sh

cat custom/features.json |jq '.features[] | "./custom/features/" + .path + "/" + .scripts.apply'|awk '{system(" chmod +x "$1)}'
cat custom/features.json |jq '.features[] | select(.active=="true") |"./custom/features/" + .path + "/" + .scripts.apply'|awk '{system($1)}'

#
# Update gulpfile
#
cp -r custom/gulpfile.js .

gulp inject
#gulp build
