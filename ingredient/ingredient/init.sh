# Pull mongodb
docker pull mongodb/mongodb-community-server

# Run mongodb as a container
docker run --name mongocontainer -d mongodb/mongodb-community-server:latest

# docker network create ${network_name}
docker network create networkmongo

# docker network connect ${network_name} ${container_name}
docker network connect networkmongo mongocontainer

# build application
mvn clean package compile install -DskipTests

# create the image
docker build -t ingredient .

# run application container using mongodb connection
docker run -d -p 5555:5555 --name ingredientcontainer --net networkmongo -e SERVER_PORT=5555 -e MONGODB_HOST=mongocontainer -e MONGODB_PORT=27017 -e MONGODB_DBNAME=meal-org -e MONGODB_USER=admin -e MONGODB_PASSWORD=admin ingredient