
feature=$1

#chmod +x ./custom/features/$category/$feature/apply-feature.sh
#./custom/features/$category/$feature/apply-feature.sh


cat custom/features.json |jq --arg feature "$feature" '.features[] | select(.id==$feature) | "./custom/features/" + .path + "/" + .scripts.apply'|awk '{system(" chmod +x "$1)}'
cat custom/features.json |jq --arg feature "$feature" '.features[] | select(.id==$feature) | "./custom/features/" + .path + "/" + .scripts.apply'|awk '{system($1)}'



#gradle assemble
