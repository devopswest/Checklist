ls -R ./custom/features/$1 | awk '
/:$/&&f{s=$0;f=0}
/:$/&&!f{sub(/:$/,"");s=$0;f=1;next}
NF&&f{ print s"/"$0 }'|sed -n "s/\(.\/custom\/features\/[A-Za-z0-9-]*\)\(.*\)/\1:\2/p"|awk -F':' '{system("cp ."$2" "$1$2)}'
