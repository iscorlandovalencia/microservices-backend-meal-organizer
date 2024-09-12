# create the image
docker build -t discoverymicroservice .

# run application container
docker run --name discoveryservicecontainer -p 8761:8761 -d discoverymicroservice