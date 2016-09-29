#!/bin/sh
echo "***************************************"
echo "***          SCANNING CODE          ***"
echo "***            1. checkstyle        ***"
echo "***            2. lint              ***"
echo "***            3. findbugs          ***"
echo "***            4. sonarq            ***"
echo "***            5. fortify           ***"
echo "***************************************"

gradle findbugsMain checkstyleMain checkThePMD fortifyReport
mvn sonar:sonar
