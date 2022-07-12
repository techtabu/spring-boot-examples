package techtabu.reactor;

import org.springframework.stereotype.Service;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.Instant;

/**
 * @author tabuthevarajan
 */

@Service
public class ReactorService {

    public Flux<String> getStringsAsFlux() {
        return Flux.just("the", "quick", "brown", "fox",
                "jumped", "over", "the", "lazy", "dog");
    }

    public ConnectableFlux<Object> createHotStream(long timeInMillies) {
        return Flux.create(fluxSink -> {
            long now = Instant.now().getEpochSecond();
            while(Instant.now().getEpochSecond() - now < 5 ) {
                fluxSink.next(Instant.now());
            }
        })
        .sample(Duration.ofMillis(timeInMillies)) // this will be like sleep, and will provide data at every 500 ms only.
        .publish();
    }
}
