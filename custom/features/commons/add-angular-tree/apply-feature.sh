echo "**************************"
echo "**** ADD-ANGULAR-TREE ****"
echo "**************************"

#
# Bower
#

# Add angular-tree-ui
bower install angular-ui-tree --save
sed -i "s/angular-loading-bar/angular-loading-bar',\n\t\t\t'ui.tree/" src/main/webapp/app/app.module.js
