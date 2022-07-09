# Getting Started

Following configuration will create database if it does not exists,
```yaml
spring:
  data:
    mongodb:
      uri: mongodb://mongodb:mongodb@localhost:27017
      database: store_orders
```

In addition, username and password as separate prop did not work for me, had to add as parameters in uri. 


## sample queries
```js
db.store_invoices.find({invoiceNumber: 'invoice-001'} )

db.store_invoices.find({"shippingAddress.city": 'Kansas City'})
```

## app stuff
find only desired fields.. 
```
@Query(value = "{invoiceNumber:'?0'}", fields = "{'customerId' : 1, 'shippingAddress': 1}")
Invoice findSpecificFields(String invoiceNumber);
```

The above will only return customerId & shippingAddress.. works like projections in hibernate.. but directly in mongo query.

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