# json serialization

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



