rm -R src
yo jhipster --force
jhipster-uml custom/design/model.jh

cp -r custom/* .

gulp build

gradle assemble
gradle
