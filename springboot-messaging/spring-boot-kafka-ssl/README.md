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

# kafka security
Security is implemented in three layers. 
* secure kafka with SSL

Encryption of data over the wire using SSL/ TLS. i.e. encoding data with security key before sending it via network. 

* Authentication using SSL or SASL.

Allows producers and consumer to authenticate to your Kafka cluster, which verifies their identity. It's also secure 
way to endorse an identity.

* Authorization using ACLs

Once your clients are authenticated, your kafka brokers can run them against access control list (ACL) to determine 
whether a particular client is authorized to read/ write to some topic. 

## Encryption using SSL/ TLS 
When SSL is enabled for a Kafka listener, all traffic for that channel will be encrypted with TLS, which employs digital certificates for identity verification.

Since TLS uses certificates, you'll need to generate them and configure your clients and brokers with the appropriate 
keys and certificates. You will also need to **periodically update** the certificates before they expire, in order to avoid TLS handshake failures

However, note that SSL comes with a cost. The process of encryption and decryption brings lag in the system. 

## Authenticating using SASL-SSL
SASL-SSL (Simple Authentication and Security Layer) uses TLS encryption like SSL but differs in its authentication process. 
To use the protocol, you must specify one of the four authentication methods supported by Apache Kafka: 
* GSSAPI: provides for authentication using Kerberos, which is a network authentication protocol. 
  * provides strong authentication for client/server applications using secret-key cryptography. 
* PLAIN: username/password mechanisms
  * These username and passwords have to be stored on the Kafka brokers in advance and each change needs a rolling start.
* SCRAM-SHA-256/512: username/password mechanisms alongside a challenge (salt), which makes it more secure.
  * Salted Challenge Response Authentication Mechanism
  * username and passwords are stored in Zookeeper, which enables scaling security without rebooting brokers. 
  * If you use this, make sure to enable SSL encryption so that credentials are not sent as PLAINTEXT via network.
* OAUTHBEARER: machine-to-machine equivalent of single sign-on 

One of the main reasons you might choose SASL-SSL over SSL is because you'd like to integrate Kafka, for example, 
with an existing Kerberos server in your organization, such as Active Directory or LDAP.

## SSL or SASL_SSL
* SSL is ideally suited for cloud environments while SSL-SASL makes sense for enterprises that already have an authentication server.

## securing traffic between brokers
Kafka ships with four protocols to inter-broker communication
* PLAINTEXT: Un-authenticated, non-encrypted channedl
* SASL_PLAINTEXT: SASL authenticated, non-encrypted channel
* SSL: SSL Channel
* SASL_SSL: SASL authenticated, SSL channel.

Kerberos is supported with only SASL plaintext & SASL SSL, and as the name suggests, SSL & SASL_SSL support SSL.

The best protocol to go with is SASL_SSL, as this suffices most of the organization's compliance needs. 

These can be configured with standard jaas/ keytabs for automating. 

Also, note that while starting to implement these ideas, it's better to open two ports on PLAINTEXT AND 
SASL_PLAINTEXT both. Once the application or kafka clients are configured, you can remove unsecured ports. Here are 
some sample properties.

```
security.inter.broker.protocol=SASL_SSL
sasl.kerberos.service.name=kafka
sasl.enabled.mechanisms=PLAIN,GSSAPI
sasl.mechanism.inter.broker.protocol=GSSAPI
listeners=SASL_SSL://<host>:9093,PLAINTEXT://<host>:9092
```