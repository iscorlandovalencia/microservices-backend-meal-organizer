# Pull mongodb
docker pull mongodb/mongodb-community-server

# docker network create ${network_name}
docker network create network-mongo

# Run mongodb as a container
docker run -d --name mongo-container --net network-mongo -p 27017:27017 mongo:latest

# docker network connect ${network_name} ${container_name}
docker network connect network-mongo mongo-container

# build application
mvn clean package compile install -DskipTests

# create the image
docker build -t ingredient-microservice .

# run application container using mongodb connection
docker run -d --name ingredient-container --net network-mongo -p 5558:5558 \
  -e SERVER_PORT=5558 \
  -e MONGODB_HOST=mongo-container \
  -e MONGODB_PORT=27017 \
  -e MONGODB_DBNAME=meal-organizer-data \
  ingredient-microservice
  #-e MONGODB_USER=admin \
  #-e MONGODB_PASSWORD=admin \

pause