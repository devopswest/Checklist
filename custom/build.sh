rm -R target
rm -R build

#./custom/features/increase-version/apply-feature.sh


gulp build
gradle assemble
docker-compose build app
docker tag andresfuentes/checklist emmanuel16/checklist
docker push emmanuel16/checklist
