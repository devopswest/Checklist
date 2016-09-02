rm -R src
rm -R .jhipster
yo jhipster --force

#jhipster-uml custom/design/model.jh
yo jhipster:import-jdl ./custom/design/model.jh --force

#cp -r custom/* .
./custom/custom.sh

gulp build

npm install
bower install

gradle assemble
gradle
