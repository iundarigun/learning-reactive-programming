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
}