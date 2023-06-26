
To enable batch processiong with spring boot JPA, you just have to add this property and set the size.
```yaml
spring:
  jpa:
    properties:
      hibernate:
        jdbc.batch_size: 5
```

It's that simple. 

When you enable hibernate statistics by making this property `true`,
```yaml
spring:
  jpa:
    properties:
      hibernate:
        generate_statistics: true
```

Run the application with `mvn spring-boot:run`, and head to [swagger ui](http://localhost:9775/swagger-ui/index.html#)
and execute make a post request. 

You will see the following in the logs,
```
Session Metrics {
    1651888 nanoseconds spent acquiring 1 JDBC connections;
    0 nanoseconds spent releasing 0 JDBC connections;
    451881 nanoseconds spent preparing 11 JDBC statements;
    19411497 nanoseconds spent executing 10 JDBC statements;
    11920690 nanoseconds spent executing 2 JDBC batches;
    0 nanoseconds spent performing 0 L2C puts;
    0 nanoseconds spent performing 0 L2C hits;
    0 nanoseconds spent performing 0 L2C misses;
    20886544 nanoseconds spent executing 1 flushes (flushing a total of 10 entities and 0 collections);
    0 nanoseconds spent executing 0 partial-flushes (flushing a total of 0 entities and 0 collections)
}
```

this line tells us, if the recordes are batch processed or not.

```11920690 nanoseconds spent executing 2 JDBC batches;```

In this case, batch size is set to 5, and processed 10 records. they are processed in 2 batches. 