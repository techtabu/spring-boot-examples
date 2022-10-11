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

Then start the kafka and zookeeper by using the given docker-compose.yaml file. 

Make sure the leave the following env variable blank.
```yaml
KAFKA_SSL_ENDPOINT_IDENTIFICATION_ALGORITHM: ''
```

Do you know why? 
> Note that by default Kafka clients verify that the hostname in the broker's URL and the hostname in the broker's 
> certificate match. This can be disabled by setting `ssl.endpoint.identification.algorithm` to an empty string on the 
> client, which can be useful in test or dev environments that use self-signed certificates.

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

# kafka authentication
## SSL
When SSL is enabled for a Kafka listener, all traffic for that channel will be encrypted with TLS, which employs digital certificates for identity verification.

Since TLS uses certificates, you'll need to generate them and configure your clients and brokers with the appropriate 
keys and certificates. You will also need to **periodically update** the certificates before they expire, in order to avoid TLS handshake failures

## SASL-SSL
SASL-SSL (Simple Authentication and Security Layer) uses TLS encryption like SSL but differs in its authentication process. 
To use the protocol, you must specify one of the four authentication methods supported by Apache Kafka: 
* GSSAPI: provides for authentication using Kerberos 
* PLAIN: username/password mechanisms
* SCRAM-SHA-256/512: username/password mechanisms
* OAUTHBEARER: machine-to-machine equivalent of single sign-on 

One of the main reasons you might choose SASL-SSL over SSL is because you'd like to integrate Kafka, for example, 
with an existing Kerberos server in your organization, such as Active Directory or LDAP.

## SSL or SASL_SSL
* SSL is ideally suited for cloud environments while SSL-SASL makes sense for enterprises that already have an authentication server.