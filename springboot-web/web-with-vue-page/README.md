# Getting Started


# Trace Interceptors
So, you can add logging to your application using TraceInterceptors.

Caveat: You should enable trace lever logging in your application and add spring aop. How do you do that,
First off, add the following to pom. 
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```

Then add the following beans,
```java
public class Application {
    @Bean
    public CustomizableTraceInterceptor interceptor() {
        var interceptor = new CustomizableTraceInterceptor();
        interceptor.setUseDynamicLogger(true);
        interceptor.setEnterMessage("Entering $[targetClassShortName]::$[methodName] with arguments $[arguments]");
        interceptor.setExitMessage("Leaving $[methodName](..) with return value $[returnValue], took $[invocationTime]ms.");
        return interceptor;
    }

    @Bean
    public Advisor traceAdvisor() {
        var pointcut = new AspectJExpressionPointcut();
//        pointcut.setExpression("execution(public * org.springframework.data.repository.Repository+.*(..))");
        pointcut.setExpression("execution(public * techtabu.web.CustomerService+.*(..))");
        return new DefaultPointcutAdvisor(pointcut, interceptor());
    }
}
```

Also, set logging level to `trace`. That's it.. you will see in methods of `CustomerService` like this.
```
2022-08-03 14:00:24.740 TRACE 82132 --- [nio-9760-exec-2] techtabu.web.CustomerService             : Entering CustomerService::getAll with arguments
2022-08-03 14:00:24.745  INFO 82132 --- [nio-9760-exec-2] techtabu.web.CustomerService             : returning all customers
2022-08-03 14:00:24.749 TRACE 82132 --- [nio-9760-exec-2] techtabu.web.CustomerService             : Leaving getAll(..) with return value [Customer(id=1, firstName=Tabu, lastName=Dev, email=tabu@gmail.com), Customer(id=2, firstName=Shalu, lastName=Tabu, email=shalu@gmal.com)], took 5ms.
```

