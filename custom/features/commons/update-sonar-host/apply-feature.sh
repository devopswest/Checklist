echo "**************************"
echo "**** FEATURE [update-sonar-host] ****"
echo "**************************"

sed -i "s|<properties>|<properties>\n\t\t<sonar.host.url>http://adc-builder.eastus.cloudapp.azure.com:9000</sonar.host.url>|" pom.xml
