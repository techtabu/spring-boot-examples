
# spring security

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