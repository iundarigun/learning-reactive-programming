package br.com.devcave.reactiveprograming.reactor

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import reactor.test.StepVerifier
import java.nio.file.Files
import java.nio.file.Path

class OperatorsTest {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Test
    fun subscribeOnSimple() {
        val flux = Flux.range(1, 5)
            .map {
                logger.info("map 1 - number $it on ${Thread.currentThread().name}")
                it
            }
            .subscribeOn(Schedulers.boundedElastic()) // Afect the two maps
            .map {
                logger.info("map 2 - number $it on ${Thread.currentThread().name}")
                it
            }

        StepVerifier.create(flux)
            .expectSubscription()
            .expectNext(1,2,3,4,5)
            .verifyComplete()
    }

    @Test
    fun publishOnSimple() {
        val flux = Flux.range(1, 5)
            .map {
                logger.info("map 1 - number $it on ${Thread.currentThread().name}")
                it
            }
            .publishOn(Schedulers.boundedElastic()) // Afect only maps after this setting
            .map {
                logger.info("map 2 - number $it on ${Thread.currentThread().name}")
                it
            }

        StepVerifier.create(flux)
            .expectSubscription()
            .expectNext(1,2,3,4,5)
            .verifyComplete()
    }

    @Test
    fun multipleSubscribeOnSimple() {
        val flux = Flux.range(1, 5)
            .subscribeOn(Schedulers.single()) // In this case we use only the fisrt subscribeOn set and ignore next
            .map {
                logger.info("map 1 - number $it on ${Thread.currentThread().name}")
                it
            }
            .subscribeOn(Schedulers.boundedElastic())
            .map {
                logger.info("map 2 - number $it on ${Thread.currentThread().name}")
                it
            }

        StepVerifier.create(flux)
            .expectSubscription()
            .expectNext(1,2,3,4,5)
            .verifyComplete()
    }

    @Test
    fun multiplePublishOnSimple() {
        val flux = Flux.range(1, 5)
            .publishOn(Schedulers.single())
            // In this case publishOn afects only the maps after that, but if set another publishOn after the first maps,
            // the nexts maps will be affects, only the one keeps as first set
            .map {
                logger.info("map 1 - number $it on ${Thread.currentThread().name}")
                it
            }
            .publishOn(Schedulers.boundedElastic())
            .map {
                logger.info("map 2 - number $it on ${Thread.currentThread().name}")
                it
            }

        StepVerifier.create(flux)
            .expectSubscription()
            .expectNext(1,2,3,4,5)
            .verifyComplete()
    }

    @Test
    fun PublishAndSubscribeOnSimple() {
        val flux = Flux.range(1, 5)
            .publishOn(Schedulers.single())
            // In this case publishOn afects the maps after that. The subscribeOn after is ignored
            .map {
                logger.info("map 1 - number $it on ${Thread.currentThread().name}")
                it
            }
            .subscribeOn(Schedulers.boundedElastic())
            .map {
                logger.info("map 2 - number $it on ${Thread.currentThread().name}")
                it
            }

        StepVerifier.create(flux)
            .expectSubscription()
            .expectNext(1,2,3,4,5)
            .verifyComplete()
    }

    @Test
    fun SubscribeAndPublishOnSimple() {
        val flux = Flux.range(1, 5)
            .subscribeOn(Schedulers.single())
            // In this case subscribeOn afects all maps before set publishOn
            .map {
                logger.info("map 1 - number $it on ${Thread.currentThread().name}")
                it
            }
            .publishOn(Schedulers.boundedElastic())
            .map {
                logger.info("map 2 - number $it on ${Thread.currentThread().name}")
                it
            }

        StepVerifier.create(flux)
            .expectSubscription()
            .expectNext(1,2,3,4,5)
            .verifyComplete()
    }

    @Test
    fun subscribeOnIO() {
        val mono = Mono.fromCallable { Files.readAllLines(Path.of("sample.txt")) }
            .log()
            .subscribeOn(Schedulers.boundedElastic())

        mono.subscribe { logger.info("$it") }

        StepVerifier.create(mono)
            .expectSubscription()
            .thenConsumeWhile {
                Assertions.assertTrue(it.isNotEmpty())
                logger.info("Size ${it.size}")
                true
            }
            .verifyComplete()
    }
}