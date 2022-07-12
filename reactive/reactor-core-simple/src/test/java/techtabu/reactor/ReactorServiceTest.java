package techtabu.reactor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SignalType;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * @author tabuthevarajan
 */

@SpringBootTest
@Slf4j
public class ReactorServiceTest {

    @Autowired ReactorService reactorService;

    @Test
    public void testSubscribeToGetStringAsFlux() {
        log.info(" \n\n *** testing testSubscribeToGetStringAsFlux *** \n\n");
        List<String> results = new ArrayList<>();
        reactorService.getStringsAsFlux().log()
                .subscribe(w -> {
                    log.info("Word: {}", w);
                    results.add(w);
                });
        log.info("Resulted strings: {}", results);
        // Resulted strings: [the, quick, brown, fox, jumped, over, the, lazy, dog]
        assertThat(results).containsExactly("the", "quick", "brown", "fox", "jumped", "over", "the", "lazy", "dog");
    }

    @Test
    public void testParallelSubscription() {
        log.info(" \n\n *** testing testParallelSubscription *** \n\n");
        List<String> results = new ArrayList<>();
        reactorService.getStringsAsFlux().log()
                .subscribeOn(Schedulers.parallel())
                .subscribe(w -> {
                    log.info("Word: {}", w);
                    results.add(w);
                });
        log.info("Resulted strings: {}", results);
        // Resulted strings: [the, quick, brown, fox, jumped, over, the, lazy, dog]
//        assertThat(results).containsExactly("the", "quick", "brown", "fox", "jumped", "over", "the", "lazy", "dog");
    }

    @Test
    public void testWithReactor() {
        Flux<String> source = Flux.just("Hello", "World");
        int nano = StepVerifier.create(source)
                .expectNext("Hello")
                .expectNext("World")
                .verifyComplete().getNano();
        log.info("Took {} ns", nano);
    }

    @Test
    public void testHotStream() {
        log.info(" \n\n *** testing testHotStream *** \n\n");

        // getting data every 500 milliseconds
        ConnectableFlux<Object> pub = reactorService.createHotStream(500);
        pub.subscribe(s -> {
                log.info("Hot stream 500 ms: {}", s);
            });
        pub.connect();

        // getting data every 200 milliseconds
        log.info(" \n\n *** testing testHotStream every 200 ms *** \n\n");
        ConnectableFlux<Object> pub2 = reactorService.createHotStream(200);
        pub2.subscribe(s -> {
            log.info("Hot stream 200 ms: {}", s);
        });
        pub2.connect();

    }
}
