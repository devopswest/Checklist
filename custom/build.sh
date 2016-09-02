
rm -R target
rm -R build

./custom/features/increase-version/apply-feature.sh


gulp build
gradle assemble
docker-compose build app
docker push andresfuentes/checklist
