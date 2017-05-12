The intent of Estimator is to track the productivity of an individual, team or organization.

Prerequisites - you must have docker and docker-compose installed.
You can install project compatible versions via:
sudo apt-get install docker.io;
sudo apt-get install docker-compose;

In order to start the system run the next commands in the project root:

docker pull mysql;
docker pull webcenter/activemq;
docker pull redis;

mvn generate-sources
mvn clean install -Dmaven.test.skip=true;

cd scripts/tools;
docker-compose up;
cd..;
docker-compose up;



