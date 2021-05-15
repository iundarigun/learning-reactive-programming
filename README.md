# Reative programming

Reative programming is different of reactive systems. Reactive programming focusing on computation through ephemeral dataflow chains—tend to be event-driven, while reactive systems—focusing on resilience and elasticity through the communication, and coordination, of distributed systems—is message-driven. Some concepts about reactive programming

- Event Driven: Publish and subscribe
- Data Streams: All works with data Streams
- Asynchronous
- Non-blocking
- Back pressure: when the progress of turning that input to output is resisted in some way. In most cases that resistance is computational speed or TPS Control. By the way, you might eventually hear someone use the word “backpressure” to actually mean something has the ability to control or handle backpressure.
- Functional/Declarative programming: different paradigm, pure functions, immutability, lambdas, etc  

### React in a cold or hot way
Observables (who publish events) can be cold or hot
- Cold: The lazy ones. Obervables only start publish events when anyone subscribe to this observable.
- Hot: The hard workers. Observables publish events, with or without subscribers. An example can be obervable who do lecture of temperature, that publish events every scheduled time.

### Initiative Reactive Streams
Is a definition and rules to define reactive programming. Reactive Streams is an initiative to provide a standard for asynchronous stream processing with non-blocking back pressure: 
- Process a pontentially unbounded number of elements
- Sequencially and asynchronously passing elements beetwen components
- And with mandatory non-blocking backpressure

In this definition, we have 4 interfaces:
- Publisher (or observable)
- Subscriber: Who read events
- Subscription: contract within publisher and subscriber - manage the backpressure
- Processor: Processing data state
https://www.reactive-streams.org/

Flow (Cold way):
- **Subcriber** subscribe on **publisher**
- Result of this, **subcription** is create.
- **Publisher** receive notification `onSubscribe` method.
- **Subscriber** requests N elements (_backpressure_) using **subscription**
- **Publisher** publish -> `onNext` method **subscriber**. This ends when:
  - **Publisher** send all the elements requested (defined by _backpressure_)
  - **Publisher** send all the elements it has. `onComplete` method to finish **subscriber** and **subscription**
  - There is an error -> `onError` method cancel **subscriber** and **subscription**



### Project Reator 
Implements reactive streams. We have others implementations, like Akka or rxJava2.
- Two composable APIs: Flux(number unbounded of elements) and Mono (one or zero elements).
- Good for microservice architecture, because Reactor offers backpressure-ready network engines for http.
- Huge range of operators, that allow us to select, filter, transform and combine streams.

https://projectreactor.io/
