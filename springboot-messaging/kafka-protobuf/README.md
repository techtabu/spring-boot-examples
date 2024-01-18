# kafka protobuf

Compile proto file to generate Java class,
```shell
# syntax
protoc -I=$SRC_DIR --java_out=$DST_DIR $SRC_DIR/addressbook.proto

# code
protoc -I=. --java_out=src/main/java/ src/main/proto/customerMessage.proto
```

* `I`: source directory (where your application’s source code lives – the current directory is used if you don’t provide a value),
* `DST_DIR`: destination directory where you want you code need to be



# helpful resources
* [spring-kafka-protobuf :hal90210](https://github.com/hal90210/spring-kafka-protobuf/tree/master)