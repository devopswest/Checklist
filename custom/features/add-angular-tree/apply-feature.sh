echo "**************************"
echo "**** ADD-ANGULAR-TREE ****"
echo "**************************"

#
# Bower
#

# Add angular-tree-ui
bower install angular-ui-tree --save
sed -i "s/angular-loading-bar/angular-loading-bar',\n\t\t\t'ui.tree/" src/main/webapp/app/app.module.js

# Ad textEditor
bower install textAngular --save
sed -i "s/angular-loading-bar/angular-loading-bar',\n\t\t\t'textAngular/" src/main/webapp/app/app.module.js
