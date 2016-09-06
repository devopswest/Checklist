echo "**************************"
echo "**** FEATURE [add-ui-grid] ****"
echo "**************************"

#
# Bower
#

# Add angular-tree-ui
bower install angular-ui-grid --save

sed -i "s/angular-loading-bar/angular-loading-bar',\n\t\t\t'ui.grid', 'ui.grid.edit', 'ui.grid.cellNav', 'ui.grid.treeView/" src/main/webapp/app/app.module.js
