# Download keycloak Image from docker
# docker run -p 9090:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:25.0.5 start-dev

# build application
mvn clean package compile install -DskipTests

# create the image
docker build -t api-gateway .

# run application container
docker run --name apigateway-container -p 9191:9191 -d api-gateway

# docker network connect ${network_name} ${container_name}
docker network connect network-mongo apigateway-container