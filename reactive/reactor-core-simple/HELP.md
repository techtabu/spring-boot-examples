# Getting Started

# Concepts

## subscribe
Note that **Nothing happens** until you subscribe. You can create publisher, but for it to push the data,
something has to subscribe to the publisher. 

By the act of subscribing, you tie the `Publisher` to a `Subscriber`, which triggers the flow of data in the whole chain. 
This is achieved internally by a single `request` signal from the `Subscriber` that is propagated upstream, 
all the way back to the source `Publisher`.

## Hot vs Cold stream
Cold streams are static and have fixed length and stops after all elements have been processed. Hot streams the opposite,
they keep on coming with no end in sight.. Eg. application logs, stock prices, Twitter feeds, mouse movements, etc.

A Cold sequence starts anew for each Subscriber, including at the source of data. Eg: HTTP call. A new HTTP request
is made for each subscription.

A Hot sequence does not start from scratch for each `Subscriber`. Rather, late subscribers receive signal from the point
onwards they subscribed. 

# `Flux`
Flux can help us to produce stream of data, which can emit `0...n` elements. Eg:
```
Flux<Integer> just = Flux.just(1, 2, 3, 4)
```

## most common ways to create Flux
```
// Creates a Flux containing the values 1, 2, 3.
Flux<Integer> integerFlux = Flux.just(1, 2, 3);

// Creates a Flux containing "Hello", "foo" and "bar".
Flux<String> stringFlux = Flux.just("Hello", "foo", "bar");

// Creates a Flux from an already existing Iterable, for example a List.
List<String> stringList = Arrays.asList("Hello", "foo", "bar");
Flux<String> fluxFromList = Flux.fromIterable(stringList);

// It works the same with Java Streams (which are not reactive).
Stream<String> stringStream = stringList.stream();
Flux<String> fluxFromStream = Flux.fromStream(stringStream);

// Creates a flux on a range.
Flux<Integer> rangeFlux = Flux.range(1, 5); // Flux(1, 2, 3, 4, 5)

// Creates a flux that generates a new value every 100 ms. 
// The value is incremental, starting at 1.
Flux<Integer> intervalFlux = Flux.interval(Duration.ofMillis(100));

// You can also create a Flux from another one, or from a Mono.
Flux<String> fluxCopy = Flux.from(fluxFromList);
```

## map data
Similar to java stream, you can map to something.. Eg:
```
Flux.just(1, 2, 3, 4)
  .log()
  .map(i -> i * 2)
  .subscribe(elements::add);
```

## parallelizing flux - concurrency
You can parallelize the `Flux` stream using, which will cause our subscription runs on different thread. For this,
`Scheduler` interface provides abstraction around asynchronous code. 
```
reactorService.getStringsAsFlux().log()
                .subscribeOn(Schedulers.parallel())
                .subscribe(w -> {
                    log.info("Word: {}", w);
                    results.add(w);
                });
log.info("Resulted strings: {}", results);
```

When you parallelize the flux, it won't wait to move the next line, until it's completed. It will keep on going.
So, you will have `results` logged in the logs, before the stream is processed..
Means, you won't be able to assert as it is now. 
You will see the logs as follows,
```
2022-07-12 10:39:36.398  INFO 39546 --- [           main] techtabu.reactor.ReactorServiceTest      : Resulted strings: []
2022-07-12 10:39:36.398  INFO 39546 --- [     parallel-1] reactor.Flux.Array.2                     : | onSubscribe([Synchronous Fuseable] FluxArray.ArraySubscription)

```
Notice that main log is logged by `main` thread, while logs for subscribe is logged by `parallel-1` thread.

## HotStream
If you have a hot stream, you can control how often it is published. You would create something like this.. 
```
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
```

From there, consumer can tell the publisher how often it needs the data. 
```
ConnectableFlux<Object> pub = reactorService.createHotStream(500);
        pub.subscribe(s -> {
                log.info("Hot stream 500 ms: {}", s);
            });
        pub.connect();
```
Make sure you call connect on the same object you subscribed for. 


# `Mono`
`Mono` is stream of `0..1` element. 

`Mono` is a Reactive Streams `Publisher` with basic rx operators that emits at most one item via the `onNext` signal 
then terminates with an `onComplete` signal (successful `Mono`, with or without value), or only emits a single `onError` signal (failed Mono).

Most Mono implementations are expected to immediately call `Subscriber.onComplete()` after having called `Subscriber#onNext(T)`. 
`Mono.never()` is an outlier: it doesn't emit any signal, which is not technically forbidden although not terribly useful 
outside of tests. On the other hand, a combination of `onNext` and `onError` is explicitly forbidden.

Eg:
```
Mono<Integer> just = Mono.just(1)
```
This looks and behaves almost exactly the same as the Flux, only this time we are limited to no more than one element.

This difference in the semantics of these two streams is very useful, 
as for example making a request to an Http server expects to receive 0 or 1 response, 
it would be inappropriate to use a Flux in this case. On the opposite, 
computing the result of a mathematical function on an interval expects one result per number in the interval. 
In this other case, using a Flux is appropriate.

## Most common ways to create a Mono
```
// Creating a Mono containing "Hello World !".
Mono<String> helloWorld = Mono.just("Hello World !");

// Creating an empty Mono
Mono<T> empty = Mono.empty();

// Creating a mono from a Callable
Mono<String> helloWorldCallable = Mono.fromCallable(() -> "Hello World !");
// Same with Java 8 method reference
Mono<User> user = Mono.fromCallable(UserService::fetchAnyUser);

// Creating a mono from a Future
CompletableFuture<String> helloWorldFuture = MyApi.getHelloWorldAsync();
Mono<String> monoFromFuture = Mono.fromFuture(helloWorldFuture);

// Creating a mono from a supplier
Random rnd = new Random();
Mono<Double> monoFromSupplier = mono.fromSupplier(rnd::nextDouble);

// You can also create a mono from another one.
Mono<Double> monoCopy = Mono.from(monoFromSupplier);
// Or from a Flux.
Mono<Integer> monoFromFlux = Mono.from(Flux.range(1, 10)); 
// The above Mono contains the first value of the Flux.
```

