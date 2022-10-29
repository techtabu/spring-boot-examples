# spring-metrics-prometheus
Application can be tested in two ways. 
* Run prometheus in docker & run application in terminal
* Run both prometheus and application in docker.

**Run prometheus in docker and application in terminal**

Run the application in terminal using,
```sh
mvn spring-boot:run
```

stand up prometheus in docker, Make sure you set the IP address in the targe section of prometheus.yml file to the IP 
address of your computer.
```sh
cd prom
docker-compose up -d
```

**Run the prometheus and application in docker**

Dockerize the application using,
```sh
mvn clean install -Pdocker-build
```

This will create following image with name `techtabu/sbe/spring-boot-prometheus` in docker,

Now execute,
```sh
cd docker
docker-compose up -d
```

Now the prometheus can be accessed in http://localhost:9090/. You can make query using,
```sh
http_server_requests_seconds_count
http_server_requests_seconds_sum
```

and other available metrics. 