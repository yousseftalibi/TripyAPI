# TripyAPI
backend spring boot 

You can run this manually but it painful. You'll need the right version of Kafka, Spark and Jdk8.
You should instead use docker. 

Pull the image:
docker pull 23051999/tripyapi

create a new file docker-compose.yaml and paste this into it

```
version: '3'
services:
  data-engine-service:
    build: .
    ports:
      - "8083:8083"
    links:
      - kafka
  zookeeper:
    image: zookeeper:3.7.0
    ports:
      - "2181:2181"
  kafka:
    image: wurstmeister/kafka
    ports:
      - "9092:9092"
    links:
      - zookeeper
    environment:
      KAFKA_ADVERTISED_HOST_NAME: kafka
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_CREATE_TOPICS: "GeoNodes:1:1,rawPlaces:1:1"
```

P.S: This assumes ports 2181, 9092 and 8083 are free. If they are not, choose ports that suit you.

Run: "docker-compose up"

The backend should be running right now with the database all ready
