# build application
mvn clean package compile install -DskipTests

# create the image
docker build -t discovery-microservice .

# run application container
docker run --name discoveryservice-container -p 8761:8761 -d discovery-microservice

# docker network connect ${network_name} ${container_name}
docker network connect networkmongo discoveryservice-container