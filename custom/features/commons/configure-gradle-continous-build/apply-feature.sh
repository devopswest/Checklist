echo "**************************"
echo "**** FEATURE [configure-gradle-continous-build] ****"
echo "**************************"

sed -i "s/dependencies {/dependencies {\n\t\tclasspath 'org.springframework.boot:spring-boot-gradle-plugin:1.2.5.RELEASE'\n\t\tclasspath 'org.springframework:springloaded:1.2.4.RELEASE'\n/" build.gradle
