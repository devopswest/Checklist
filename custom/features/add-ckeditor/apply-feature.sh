echo "**************************"
echo "**** FEATURE [add-ckeditor] ****"
echo "**************************"

bower install ckeditor --save
bower install ng-ckeditor --save

sed -i "s/angular-loading-bar/angular-loading-bar',\n\t\t\t'ngCkeditor/" src/main/webapp/app/app.module.js
