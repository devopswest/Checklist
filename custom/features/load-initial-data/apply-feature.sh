echo "**********************************************"
echo "**** ADD-LOAD-DATA-LIQUIDBASE CHANGE LOGS ****"
echo "**********************************************"

#
# DB Updates
#
cp -r custom/features/load-initial-data/src/main/resources/config/liquibase src/main/resources/config
sed -i "s|<\/databaseChangeLog>| \
\t<include file=\"classpath:config/liquibase/changelog/30000000000000_load_data.xml\" relativeToChangelogFile=\"false\"/> \
\n</databaseChangeLog> \
|" src/main/resources/config/liquibase/master.xml

export PGPASSWORD='pwc123';

#psql -h adc-database.eastus.cloudapp.azure.com -p 5432 -U postgres
