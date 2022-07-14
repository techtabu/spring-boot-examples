package techtabu.reactor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * @author TechTabu
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
        // output
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
        // output
        // Resulted strings: []

        // you won't see any in output, because parallel will run in separate thread while letting the
        // main thread go on in its business as usual.
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

    @Test
    public void testGetElementsWithDelay() {
        log.info(" \n\n *** testing testGetElementsWithDelay *** \n\n");

        int nano = StepVerifier.create(reactorService.getElementsWithDelay(100))
                    .expectNext("the")
                    .expectNext("quick")
                    .expectNext("brown")
                    .expectNext("fox")
                    .verifyComplete()
                    .getNano();
        log.info("Took {} nano seconds with delay", nano);

        assertThat(nano).isGreaterThan(400000000);
    }
}
