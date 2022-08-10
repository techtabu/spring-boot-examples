# Getting Started

# app set up
Following configuration will create database if it does not exists,
```yaml
spring:
  data:
    mongodb:
      uri: mongodb://mongodb:mongodb@localhost:27017
      database: store_orders
```

In addition, username and password as separate prop did not work for me, had to add as parameters in uri. 

According to mongo documents, it should work. Should try again.

## `@EnableMongoRepositories`
Here is the deal with this annotation. 

> Spring Boot automatically handles those repositories as long as they are included in the same 
> package (or a sub-package) of your `@SpringBootApplication` class. 
> For more control over the registration process, you can use the @EnableMongoRepositories annotation.

By default, `@EnableMongoRepositories` scans the current package for any interfaces that extend one of Spring Data’s repository interfaces. 
You can use its `basePackageClasses=MyRepository.class` to safely tell Spring Data MongoDB to scan a different 
root package by type if your project layout has multiple projects and it does not find your repositories.

So, as long as I keep the application simple and all packages under root package, **I don't need** `@EnableMongoRepositories` 

# connecting to mongo


## `MongoDatabaseFactory`
To access MongoDB databases, you can inject an auto-configured `org.springframework.data.mongodb.MongoDatabaseFactory`. 
By default, the instance tries to connect to a MongoDB server at mongodb://localhost/test.

The thing is this access Mongo as some basic level, which means you won't be able to use advantages of high level
apis and feature of `MongoTemplate` and spring data. You probably won't be needing this.

If you have defined your own `MongoClient`, it will be used to auto-configure a suitable `MongoDatabaseFactory`.

The auto-configured `MongoClient` is created using a `MongoClientSettings` bean. 
If you have defined your own `MongoClientSettings`, it will be used without modification and the 
`spring.data.mongodb` properties will be ignored. 
Otherwise a `MongoClientSettings` will be auto-configured and will have the `spring.data.mongodb` properties applied to it. 
In either case, you can declare one or more `MongoClientSettingsBuilderCustomizer` beans to fine-tune the `MongoClientSettings` configuration. 
Each will be called in order with the `MongoClientSettings.Builder` that is used to build the `MongoClientSettings`.


## `MongoTemplate`
Spring Data MongoDB provides a `MongoTemplate` class that is very similar in its design to Spring’s `JdbcTemplate`. 

Spring Data MongoDB uses the MongoTemplate to execute the queries behind your find* methods.
You can use the template yourself for more complex queries.

# Mongo db queries
## direct queries
```js
db.store_invoices.find({invoiceNumber: 'invoice-001'} )

db.store_invoices.find({"shippingAddress.city": 'Kansas City'})
```

## spring data stuff
find only desired fields.. 
```
@Query(value = "{invoiceNumber:'?0'}", fields = "{'customerId' : 1, 'shippingAddress': 1}")
Invoice findSpecificFields(String invoiceNumber);
```

The above will only return customerId & shippingAddress.. works like projections in hibernate. but directly in mongo query.

will return,
```json
{
  "id": "62c9c0d1f22a6b2a6edee4b9",
  "invoiceNumber": null,
  "customerId": "customer-001",
  "orders": [],
  "shippingAddress": {
    "street1": "141 Main Road",
    "street2": null,
    "state": "KS",
    "city": "Kansas City",
    "country": "USA",
    "zipCode": 34453
  }
}
```