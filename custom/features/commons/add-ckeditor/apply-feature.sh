echo "**************************"
echo "**** FEATURE [add-ckeditor] ****"
echo "**************************"


#
# http://naveensingh.net/how-to-use-ckeditor-in-angularjs-with-custom-directive/
# http://ckeditor.com/blog/CKEditor-Supports-Bower-and-Composer
# http://jsbin.com/focolox/1/edit?html,js,output
#
#sed -i "s/\"resolutions\": {/\"resolutions\": { \"angular\": \"1.5.8\",/" bower.json


#
# NOTE: Order is important
#
bower install ng-ckeditor --save
bower install ckeditor --save

sed -i "s/angular-loading-bar/angular-loading-bar',\n\t\t\t'ngCkeditor/" src/main/webapp/app/app.module.js
