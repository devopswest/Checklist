echo "******************************"
echo "**** ADD-RICH TEXT EDITOR ****"
echo "******************************"

#
# Bower
#

# Ad textEditor
bower install textAngular --save
sed -i "s/angular-loading-bar/angular-loading-bar',\n\t\t\t'textAngular/" src/main/webapp/app/app.module.js
