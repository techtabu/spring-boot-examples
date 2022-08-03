package techtabu.web;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.interceptor.CustomizableTraceInterceptor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class WebApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

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
