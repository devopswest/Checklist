rm -R src
rm -R .jhipster
rm -R build
rm -R target
yo jhipster --force

npm install
bower install

#jhipster-uml custom/design/model.jh
yo jhipster:import-jdl ./custom/design/model.jh --force

./custom/custom.sh
bower install

gulp build
gradle assemble
gradle
