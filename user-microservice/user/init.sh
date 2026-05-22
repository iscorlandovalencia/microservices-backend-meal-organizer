# Pull mongodb
docker pull mongo

# docker network create ${network_name}
docker network create network-mongo

# Run mongodb as a container
docker run -d --name mongo-container --net network-mongo -p 27018:27017 mongo:latest

# docker network connect ${network_name} ${container_name}
docker network connect network-mongo mongo-container

# build application
mvn clean package compile install -DskipTests

# create the image
docker build -t user-microservice .

# run application container using mongodb connection
docker run -d --name user-container --net network-mongo -p 4445:4445 \
  -e SERVER_PORT=4445 \
  -e MONGODB_HOST=mongo-container \
  -e MONGODB_PORT=27017 \
  -e MONGODB_DBNAME=meal-organizer-data \
  user-microservice
  #-e MONGODB_USER=admin \
  #-e MONGODB_PASSWORD=admin \
