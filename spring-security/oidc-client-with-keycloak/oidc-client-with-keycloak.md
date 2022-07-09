
# spring security

oauth2-client dependency is required,
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-client</artifactId>
</dependency>
```

To make `POST` request work, you have to disable csrf using,
```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
            .cors().and().csrf().disable()
            // rest of the config
            ;
    return http.build();
}
```