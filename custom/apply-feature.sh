
category="development"
feature=$1

chnmod +x ./custom/features/$category/$feature/apply-feature.sh
./custom/features/$category/$feature/apply-feature.sh

#gradle assemble
