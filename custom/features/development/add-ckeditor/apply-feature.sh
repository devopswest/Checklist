echo "**************************"
echo "**** FEATURE [add-ckeditor] ****"
echo "**************************"


#
# http://naveensingh.net/how-to-use-ckeditor-in-angularjs-with-custom-directive/
# http://ckeditor.com/blog/CKEditor-Supports-Bower-and-Composer
# http://jsbin.com/focolox/1/edit?html,js,output
#

bower install ckeditor --save
bower install ng-ckeditor --save

sed -i "s/angular-loading-bar/angular-loading-bar',\n\t\t\t'ngCkeditor/" src/main/webapp/app/app.module.js
