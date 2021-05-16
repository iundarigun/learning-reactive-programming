package br.com.devcave.reactiveprograming.reactor

import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class MonoTest {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Test
    fun monoSubscriber() {
        val name = "iundarigun"
        val mono = Mono.just(name).log()

        mono.subscribe()

        logger.info("-------")

        StepVerifier.create(mono)
            .expectNext(name)
            .verifyComplete()
    }

    @Test
    fun monoSubscriberConsumer() {
        val name = "iundarigun"
        val mono = Mono.just(name).log()

        mono.subscribe {
            logger.info("Value $it")
        }

        logger.info("-------")

        StepVerifier.create(mono)
            .expectNext(name)
            .verifyComplete()
    }

    @Test
    fun monoSubscriberConsumerError() {
        val name = "iundarigun"
        val mono = Mono.just(name).map { throw RuntimeException("error testing mono") }.log()

        mono.subscribe(
            { logger.info("Value $it") },
            { logger.error("something is wrong", it) }
        )

        logger.info("-------")

        StepVerifier.create(mono)
            .expectError(RuntimeException::class.java)
            .verify()
    }

    @Test
    fun monoSubscriberComplete() {
        val name = "iundarigun"
        val mono = Mono.just(name)
            .log()
            .map { it.toUpperCase() }

        mono.subscribe(
            { logger.info("Value $it") },
            { logger.error("something is wrong", it) },
            { logger.info("Finished!") }
        )

        logger.info("-------")

        StepVerifier.create(mono)
            .expectNext(name.toUpperCase())
            .verifyComplete()
    }

    @Test
    fun monoSubscriberSubscription() {
        val name = "iundarigun"
        val mono = Mono.just(name)
            .log()
            .map { it.toUpperCase() }

        mono.subscribe(
            { logger.info("Value $it") },
            { logger.error("something is wrong", it) },
            { logger.info("Finished!") },
            { it.cancel() }
        )

        logger.info("-------")

        StepVerifier.create(mono)
            .expectNext(name.toUpperCase())
            .verifyComplete()
    }

    @Test
    fun monoDoOnMethod() {
        val name = "iundarigun"
        val mono = Mono.just(name)
            .log()
            .map { it.toUpperCase() }
            .doOnSubscribe { logger.info("subscribed") }
            .doOnRequest { logger.info("Request received, starting doing something") }
            .doOnNext { logger.info("Value is here. Executing doOnNext $it") }
            .doOnSuccess { logger.info("doOnSuccess executed") }

        mono.subscribe(
            { logger.info("Value $it") },
            { logger.error("something is wrong", it) },
            { logger.info("Finished!") }
        )
    }

    @Test
    fun monoDoOnError() {
        val error = Mono.error<Any> { IllegalArgumentException("illegal argument exception") }
            .doOnError { logger.error("error message: ${it.message}") }
            .doOnNext { logger.info("This is not executed because onError cancel subscription") }
            .log()

        StepVerifier.create(error)
            .expectError(IllegalArgumentException::class.java)
            .verify()
    }

    @Test
    fun monoDoOnErrorResume() {
        val name = "iundarigun"
        val error = Mono.error<Any> { IllegalArgumentException("illegal argument exception") }
            .doOnError { logger.error("error message: ${it.message}") }
            .onErrorResume {
                logger.info("error resuming")
                Mono.just(name)
            }
            .log()

        StepVerifier.create(error)
            .expectNext(name)
            .verifyComplete()
    }

    @Test
    fun monoDoOnErrorReturn() {
        val name = "iundarigun"
        val error = Mono.error<Any> { IllegalArgumentException("illegal argument exception") }
            .doOnError { logger.error("error message: ${it.message}") }
            .onErrorReturn(name)
            .log()

        StepVerifier.create(error)
            .expectNext(name)
            .verifyComplete()
    }
}