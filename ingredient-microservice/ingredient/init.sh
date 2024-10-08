# Pull mongodb
docker pull mongodb/mongodb-community-server

# Run mongodb as a container
docker run -d -p 27017:27017 --name mongocontainer -d mongodb/mongodb-community-server:latest

# docker network create ${network_name}
docker network create networkmongo

# docker network connect ${network_name} ${container_name}
docker network connect networkmongo mongocontainer

# build application
mvn clean package compile install -DskipTests

# create the image
docker build -t ingredient-microservice .

# run application container using mongodb connection
docker run --name ingredientcontainer --net networkmongo -e SERVER_PORT=5555 -e MONGODB_HOST=mongocontainer -e MONGODB_PORT=27017 -e MONGODB_DBNAME=meal-org -e MONGODB_USER=admin -e MONGODB_PASSWORD=admin -d ingredient-microservice