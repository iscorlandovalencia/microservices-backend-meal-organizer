# Pull mongodb
docker pull mongo

# Run mongodb as a container
docker run -d -p 27017:27017 --name mongocontainer -d mongo:latest

# docker network create ${network_name}
docker network create networkmongo

# docker network connect ${network_name} ${container_name}
docker network connect networkmongo mongocontainer

# build application
mvn clean package compile install -DskipTests

# create the image
docker build -t user-microservice .

# run application container using mongodb connection
docker run --name usercontainer --net networkmongo -e SERVER_PORT=4445 -e MONGODB_HOST=mongocontainer -e MONGODB_PORT=27017 -e MONGODB_DBNAME=meal-org -e MONGODB_USER=admin -e MONGODB_PASSWORD=admin -d user-microservice