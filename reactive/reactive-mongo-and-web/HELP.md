# Getting Started

### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.7.1/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.7.1/maven-plugin/reference/html/#build-image)
* [Spring Reactive Web](https://docs.spring.io/spring-boot/docs/2.7.1/reference/htmlsingle/#web.reactive)
* [Spring Data Reactive MongoDB](https://docs.spring.io/spring-boot/docs/2.7.1/reference/htmlsingle/#data.nosql.mongodb)
* [Webflux function](https://docs.spring.io/spring-framework/docs/5.3.21/reference/html/web-reactive.html#webflux-fn)

### Guides

The following guides illustrate how to use some features concretely:

* [Building a Reactive RESTful Web Service](https://spring.io/guides/gs/reactive-rest-service/)
* [Accessing Data with MongoDB](https://spring.io/guides/gs/accessing-data-mongodb/)


# `ReactiveMongoRepository`

Here is the thing. Just calling, `reativeMongoReposotory.save(<T>)` will not save it to db.
i.e. until you subscribe to it, it won't save it. 
When you leaving the controller (mab be the one's annotated as `@Transactional` it may happen),
but if you call the `save` method just from inside the object will not be saved. Eg:
```
public void createNumberOfCustomers(@RequestParam Integer count) {
    IntStream.range(0, count)
            .forEach(i -> customerService.saveCustomer(createRandomCustomer()).subscribe());
}
```

# controller for webflux
here is the thing, if we request content without using an `Accept` header, or we set it to `application/json`,
we will get the fucking blocking process and a JSON formatted response. 

If we want to go full reactive and use [Server Sent Events](https://www.w3schools.com/html/html5_serversentevents.asp)
support in the Spring to implement our full reactive stack, we have to support an Event-Stream response.
Spring to rescue again.. i.e. we need to set `Accept` header to `text/event-stream`, therefore activating
the reactive functionality in Spring to open an SSE channel and publish server-to-client events. 

Also, make sure you specify `text/event-stream` as return type. 
```
@GetMapping(value = "/paginated", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
public Flux<Customer> findAllPaginated(@RequestParam("delay") Long delay,
                                       @RequestParam int page,
                                       @RequestParam int size) {
    return customerService.getAllCustomersPaginated(delay, page, size);
}
```

## testing
To test, use the browser, instead of postman, since Postman will block until all the content is reurned.
When you go [here](http://localhost:9875/reactive-mongo-with-web/paginated?delay=100&page=10&size=20),
you will see browser will updates values as they become available one by one at 100 ms interval.