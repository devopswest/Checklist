echo "**************************"
echo "**** FEATURE [increase-version] ****"
echo "**************************"



#
# Code updates
#

sed -i "s|version = '0.0.1-SNAPSHOT'|version = '0.0.2-SNAPSHOT'|" build.gradle
sed -i "s|<version>0.0.1-SNAPSHOT</version>|<version>0.0.2-SNAPSHOT</version>|" pom.xml
sed -i "s|0.0.1-SNAPSHOT|0.0.2-SNAPSHOT|" src/main/webapp/app/app.constants.js

