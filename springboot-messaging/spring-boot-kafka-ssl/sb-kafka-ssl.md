# spring boot kafka ssl

# set up
## start kafka with SSL
To generate ssl run the given kafka-generate-ssl.sh script. This can be downloaded by given command.
```shell
curl https://raw.githubusercontent.com/confluentinc/confluent-platform-security-tools/master/kafka-generate-ssl.sh -o kafka-generate-ssl.sh
./kafka-generate-ssl.sh
```

This will generate three files in keystore and truststore directories. copy them in one location.
```shell
cp ./keystore/kafka.keystore.jks ./certs
cp ./truststore/kafka.truststore.jks ./certs
cp ./truststore/ca-key ./certs
```

Then start the kafka and zookeeper by using the given docker-compose.yaml file. Make sure the leave the following env variable blank.
```yaml
KAFKA_SSL_ENDPOINT_IDENTIFICATION_ALGORITHM: ''
```

## springboot ssl configuration
Look at the application.yaml file for configuration option. However, make sure set the protocol in kafka.properties section.
```yaml
spring:
  kafka:
    properties:
      ssl.endpoint.identification.algorithm:
      security.protocol: SSL
```

Also, make sure `ssl.endpoint.identification.algorithm` to leave blank to avoid hostname verification for localhost. Otherwise,
spring will throw fit. 

That's all. Happy streaming!

# basic commands
