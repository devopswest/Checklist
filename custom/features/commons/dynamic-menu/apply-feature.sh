echo "**************************"
echo "**** ADD-DYNAMIC MENU ****"
echo "**************************"


#
# Navigator
#
cp -r custom/features/commons/dynamic-menu/src/main/webapp/app/layouts/navbar src/main/webapp/app/layouts

#
# style="visibility:hidden;display:none" ng-class="{active: vm.$state.includes('entity')}"
#
sed -i "s|ng-class=\"{active: vm.\$state.includes('entity')}\"|style=\"visibility:hidden;display:none\" ng-class=\"{active: vm.\$state.includes('entity')}\"|" src/main/webapp/app/layouts/navbar/navbar.html

#
# Add menu script
#
sed -i "s|<li ng-class=\"{active: vm.\$state.includes('account')}\" uib-dropdown class=\"dropdown pointer\">| \
<!-- NEW --> \
                <li has-any-authority=\"{{option.authorities}}\" ng-repeat=\"option in vm.menu track by option.id\" \
                    ng-class=\"{active: vm.\$state.includes(option.id)}\" \
                    ng-switch-when=\"true\" \
                    uib-dropdown class=\"dropdown pointer\"> \
                    <a class=\"dropdown-toggle\" uib-dropdown-toggle href=\"\" id=\"{{option.id}}-menu\"> \
                        <span> \
                            <span class=\"{{option.uiclass}}\"></span> \
                            <span class=\"hidden-sm\" XXtranslate=\"global.menu.entities.main\"> \
                                {{option.label}} \
                            </span> \
                            <b class=\"caret\"></b> \
                        </span> \
                    </a> \
                    <ul class=\"dropdown-menu\" uib-dropdown-menu> \
                      <li has-any-authority=\"{{option.authorities}}\" ng-repeat=\"item in option.options track by item.id\" \
                          ui-sref-active=\"active\" > \
                            <a href=\"{{vm.\$state.href(item.id)}}\" ng-click=\"vm.collapseNavbar()\"> \
                                <span class=\"{item.uiclass}\"></span>\&nbsp; \
                                <span XXtranslate=\"global.menu.entities.country\">{{item.label}}</span> \
                            </a> \
                        </li> \
                    </ul> \
                </li> \
<!-- NEW --> \
<li ng-class=\"{active: vm.\$state.includes('account')}\" uib-dropdown class=\"dropdown pointer\">|" src/main/webapp/app/layouts/navbar/navbar.html


#
# Change Account top label to show user o nit when logged in
#
# <a class="dropdown-toggle" uib-dropdown-toggle href="" id="account-menu">
#
sed -i -e "s|\(<a class=\"dropdown-toggle\" uib-dropdown-toggle href=\"\" id=\"account-menu\">\)\(\([\\n]\)\?\|\(.\(\?\!<a\)\)\)*\(<\/a>\)|XXX|" src/main/webapp/app/layouts/navbar/navbar.html

sed -i -e "s|\(<a class=\"dropdown-toggle\" uib-dropdown-toggle href=\"\" id=\"account-menu\">\)\(\([\\n]\)\?\|\(.\(\?\!<a\)\)\)*\(<\/a>\)| \
<a class=\"dropdown-toggle\" uib-dropdown-toggle href=\"\" id=\"account-menu\"> \
                        <span ng-switch-when=\"true\"> \
                            <span class=\"glyphicon glyphicon-user\"></span> \
                            <span class=\"hidden-sm\" translateXX=\"global.menu.account.main\"> \
                                {{vm.account.firstName}} {{vm.account.lastName}}\
                            </span> \
                            <b class=\"caret\"></b> \
                        </span> \
                         <span ng-switch-when=\"false\"> \
                            <span class=\"glyphicon glyphicon-user\"></span> \
                            <span class=\"hidden-sm\" translate=\"global.menu.account.main\"> \
                                Account \
                            </span> \
                            <b class=\"caret\"></b> \
                        </span> \
                    </a>|" src/main/webapp/app/layouts/navbar/navbar.html

