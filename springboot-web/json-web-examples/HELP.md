# json serialization

important this,
json key is always going to be string.. 
[stackoverflow](https://stackoverflow.com/questions/11628698/can-we-make-object-as-key-in-map-when-using-json)

# `Map<Object, String>` Serialization

Without Json serialization, returning a `Map<PersonKey, Person>` will create output like this,
```json
{
    "PersonKey(firstName=Neel, lastName=Tabu)": {
        "firstName": "Neel",
        "email": "neel@email.com",
        "salary": 43000
    },
    "PersonKey(firstName=Tabu, lastName=Dev)": {
        "firstName": "Tabu",
        "email": "tabu@email.com",
        "salary": 45000
    }
}
```
i.e. Keys for the object will be a string, not a json object. Or probably the one serialized by lombok.. 

## using `toString` method
Simply by overriding the `toString` method of the key class, we can change.. For example,
```
@JsonValue
@Override
public String toString() {
    return "{firstName: " + firstName + ", lastName: " + lastName + "}";
}
```

will convert the return response like this..
```json
{
    "{firstName: Tabu, lastName: Dev}": {
        "firstName": "Tabu",
        "email": "tabu@email.com",
        "salary": 45000
    },
    "{firstName: Neel, lastName: Tabu}": {
        "firstName": "Neel",
        "email": "neel@email.com",
        "salary": 43000
    }
}

```

## using `JSONSerializer`

If you don't want to override the `toString` method for logging or other purposes, you can provide 
a `JSONSerializer`. But that will complicates thing a bit.. 
1. You have to provide a class level variable... because `@JsonSerialize` can only be used at class level variable
2. Too much code

Let's see how we can do.. First, instead of overriding the `toString` method, provide an serialization method..
```
@JsonValue
public String convertToKey() {
    return "{firstName: " + firstName + ", lastName: " + lastName + "}";
}
```

Then, create a JSONSerializer class for your preferred class,
```java
public class PersonKeyWithJSerializer extends JsonSerializer<PersonKeyWithJ> {

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void serialize(PersonKeyWithJ value,
                          JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        StringWriter writer = new StringWriter();
        mapper.writeValue(writer, value.convertToKey());
        jsonGenerator.writeFieldName(writer.toString());
    }
}
```

You can directly create serialization here as well. 

Then, you have create variable at class level.. because, @JsonSerialize is not applicable to local variable.
```
@JsonSerialize(keyUsing = PersonKeyWithJSerializer.class)
private Map<PersonKeyWithJ, Person> map;
```

Then, you can use this map in the place you want.. 
```
@GetMapping("/byserializer")
public Map<PersonKeyWithJ, Person> getAllPersonsByPersonKeyWithJ() {

    map = Map.of(new PersonKeyWithJ("Shalu", "Tabu"), new Person("Shalu", "shalu@email.com", 45000),
            new PersonKeyWithJ("Inara", "Tabu"), new Person("Inara", "inara@email.com", 43000));
    return map;
}
```
So, I don't like this. Let's stick to overriding `toString` method. 




