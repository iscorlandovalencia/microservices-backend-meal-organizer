# Pull mongodb
docker pull mongodb/mongodb-community-server

# docker network create ${network_name}
docker network create network-mongo

# Run mongodb as a container
docker run -d -p 27017:27017 --name mongo-container --net network-mongo mongo:latest

# docker network connect ${network_name} ${container_name}
docker network connect network-mongo mongo-container

# build application
mvn clean package compile install -DskipTests

# create the image
docker build -t meal-microservice .

# run application container using mongodb connection
docker run -d --name meal-container --net network-mongo \
  -e SERVER_PORT=6666 \
  -e MONGODB_HOST=mongo-container \
  -e MONGODB_PORT=27017 \
  -e MONGODB_DBNAME=meal-organizer-data \
  meal-microservice
  #-e MONGODB_USER=admin \
  #-e MONGODB_PASSWORD=admin \
