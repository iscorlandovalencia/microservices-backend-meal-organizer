# build application
mvn clean package compile install -DskipTests

# create the image
docker build -t discoverymicroservice .

# run application container
docker run --name discoveryservicecontainer -p 8761:8761 -d discoverymicroservice

# docker network connect ${network_name} ${container_name}
docker network connect networkmongo discoveryservicecontainer