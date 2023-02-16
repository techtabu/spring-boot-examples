# spring-boot-kafka


## create new topics
To create new topics when the application start up, you just have to define a bean with name.
```
@Bean
public NewTopic topicByBoot() {
    return TopicBuilder.name("topic_By_boot").build();
}
```

