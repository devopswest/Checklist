#
# Bower
#

# Add angular-tree-ui
bower install angular-ui-tree --save
sed -i "s/angular-loading-bar/angular-loading-bar',\n\t\t\t'ui.tree/" src/main/webapp/app/app.module.js


#
# CSS - Add Custom styles
#
cp -r custom/src/main/webapp/scss src/main/webapp
cp -r custom/src/main/webapp/content src/main/webapp
cp -r custom/src/main/webapp/app/home src/main/webapp/app


sed -i "s|</head>|\t<!-- build:css content/css/custom.css -->\n\t<link rel="stylesheet" href="content/css/custom.css">\n\t<!-- endbuild -->\n</head>|" src/main/webapp/index.html


#
# Navigator
#
cp -r custom/src/main/webapp/app/layouts/navbar src/main/webapp/app/layouts

sed -i "s|logo-jhipsterl|ogo.png|" src/main/webapp/app/layouts/navbar/navbar.html
#sed -i -e 's|\(<a class="dropdown-toggle" uib-dropdown-toggle href="" id="account-menu">\)\(\([\n]\)?|\(.\(?!<a\)\)\)*\(<\/a>\)|XXX|' src/main/webapp/app/layouts/navbar/navbar.html

sed -i -e 's|\(<a class="dropdown-toggle" uib-dropdown-toggle href="" id="account-menu">\)\(\([\n]\)?|\(.\(?!<a\)\)\)*\(<\/a>\)| \
<a class="dropdown-toggle" uib-dropdown-toggle href="" id="account-menu"> \
                        <span ng-switch-when="true"> \
                            <span class="glyphicon glyphicon-user"></span> \
                            <span class="hidden-sm" translateXX="global.menu.account.main"> \
                                {{vm.account.firstName}} \
                            </span> \
                            <b class="caret"></b> \
                        </span> \
                         <span ng-switch-when="false"> \
                            <span class="glyphicon glyphicon-user"></span> \
                            <span class="hidden-sm" translate="global.menu.account.main"> \
                                Account \
                            </span> \
                            <b class="caret"></b> \
                        </span> \
                    </a>|' src/main/webapp/app/layouts/navbar/navbar.html





#
# Update gulpfile
#
cp -r custom/gulpfile.js .

gulp inject
#gulp build
