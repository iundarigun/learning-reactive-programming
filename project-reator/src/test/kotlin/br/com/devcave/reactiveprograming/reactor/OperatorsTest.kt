package br.com.devcave.reactiveprograming.reactor

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import reactor.blockhound.BlockHound
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import reactor.test.StepVerifier
import java.nio.file.Files
import java.nio.file.Path
import java.time.Duration

class OperatorsTest {

    private val logger = LoggerFactory.getLogger(javaClass)

    companion object {
        @BeforeAll
        @JvmStatic
        internal fun setUp() {
            BlockHound.install()
        }
    }

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
            .expectNext(1, 2, 3, 4, 5)
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
            .expectNext(1, 2, 3, 4, 5)
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
            .expectNext(1, 2, 3, 4, 5)
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
            .expectNext(1, 2, 3, 4, 5)
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
            .expectNext(1, 2, 3, 4, 5)
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
            .expectNext(1, 2, 3, 4, 5)
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

    @Test
    fun switchIfEmptyOperator() {
        // For example, if we are getting value form database, and we want some behavior if the result is empty
        val flux = emptyFlux().switchIfEmpty(Flux.just("not empty anymore"))
            .log()

        StepVerifier.create(flux)
            .expectSubscription()
            .expectNext("not empty anymore")
            .expectComplete()
            .verify()
    }

    @Test
    fun deferOperator() {
        val just = Mono.just(System.currentTimeMillis()) // Capture value on instance moment
        val defer = Mono.defer { Mono.just(System.currentTimeMillis()) } // Capture value on subscription

        Thread.sleep(100L)
        just.subscribe { logger.info("time just $it") }
        defer.subscribe { logger.info("time defer $it") }
        Thread.sleep(100L)
        just.subscribe { logger.info("time just $it") }
        defer.subscribe { logger.info("time defer $it") }
        Thread.sleep(100L)
        just.subscribe { logger.info("time just $it") }
        defer.subscribe { logger.info("time defer $it") }
        Thread.sleep(100L)
        just.subscribe { logger.info("time just $it") }
        defer.subscribe { logger.info("time defer $it") }
    }

    @Test
    fun concatOperator() {
        val flux1 = Flux.just("a", "b")
        val flux2 = Flux.just("c", "d")

        val concatFux = Flux.concat(flux1, flux2).log()
        StepVerifier.create(concatFux)
            .expectSubscription()
            .expectNext("a", "b", "c", "d")
            .expectComplete()
            .verify()
    }

    @Test
    fun concatWithOperator() {
        val flux1 = Flux.just("a", "b")
        val flux2 = Flux.just("c", "d")

        val concatFux = flux1.concatWith(flux2).log()

        StepVerifier.create(concatFux)
            .expectSubscription()
            .expectNext("a", "b", "c", "d")
            .expectComplete()
            .verify()
    }

    @Test
    fun concatOperatorError() {
        val flux1 = Flux.just("a", "b")
            .map {
                if (it == "b") {
                    throw IllegalArgumentException()
                }
                it
            }
        val flux2 = Flux.just("c", "d")

        val concatFux = Flux.concat(flux1, flux2).log()
        StepVerifier.create(concatFux)
            .expectSubscription()
            .expectNext("a")
            .expectError()
            .verify()
    }

    @Test
    fun concatOperatorDelayError() {
        val flux1 = Flux.just("a", "b")
            .map {
                if (it == "b") {
                    throw IllegalArgumentException()
                }
                it
            }
        val flux2 = Flux.just("c", "d")

        // Let erro to the end of concat
        val concatFux = Flux.concatDelayError(flux1, flux2).log()

        StepVerifier.create(concatFux)
            .expectSubscription()
            .expectNext("a", "c", "d")
            .expectError()
            .verify()
    }

    @Test
    fun combineLastOperator() {
        val flux1 = Flux.just("a", "b")
        val flux2 = Flux.just("c", "d")

        val combineLatest = Flux.combineLatest(flux1, flux2) { s1, s2 ->
            s1.toUpperCase() + s2.toUpperCase()
        }.log()

        StepVerifier.create(combineLatest)
            .expectSubscription()
            .expectNext("BC", "BD")
            // This result is not garanted because combinelast dont control the order of publishers
            .expectComplete()
            .verify()
    }

    @Test
    fun mergeOperator() {
        val flux1 = Flux.just("a", "b").delayElements(Duration.ofMillis(200))
        val flux2 = Flux.just("c", "d")

        val mergeFlux = Flux.merge(flux1, flux2).log()
        // Concat wait all emition, and merge happends in order of publishing
        mergeFlux.subscribe { logger.info("emition $it") }

        Thread.sleep(1000L)
        StepVerifier.create(mergeFlux)
            .expectSubscription()
            .expectNext("c", "d", "a", "b")
            .expectComplete()
            .verify()
    }

    @Test
    fun mergeWithOperator() {
        val flux1 = Flux.just("a", "b").delayElements(Duration.ofMillis(200))
        val flux2 = Flux.just("c", "d")

        val mergeFlux = flux1.mergeWith(flux2)
            .log()
        // Concat wait all emition, and merge happends in order of publishing
        mergeFlux.subscribe { logger.info("emition $it") }

        Thread.sleep(1000L)
        StepVerifier.create(mergeFlux)
            .expectSubscription()
            .expectNext("c", "d", "a", "b")
            .expectComplete()
            .verify()
    }

    @Test
    fun mergeSequentialOperator() {
        val flux1 = Flux.just("a", "b").delayElements(Duration.ofMillis(200))
        val flux2 = Flux.just("c", "d")

        // Get first flux at the end, after this get second flux at the end and so on
        val mergeFlux = Flux.mergeSequential(flux1, flux2, flux1)
            .log()
        mergeFlux.subscribe { logger.info("emition $it") }

        Thread.sleep(1000L)
        StepVerifier.create(mergeFlux)
            .expectSubscription()
            .expectNext("a", "b", "c", "d", "a", "b")
            .expectComplete()
            .verify()
    }

    @Test
    fun mergeDelayErrorOperator() {
        val flux1 = Flux.just("a", "b")
            .map {
                if (it == "b") {
                    throw IllegalArgumentException()
                }
                it
            }.doOnError { logger.error("we could do something") }
        val flux2 = Flux.just("c", "d")

        // Get first flux at the end, after this get second flux at the end and so on
        val mergeFlux = Flux.mergeDelayError(1, flux1, flux2, flux1)
            .log()

        StepVerifier.create(mergeFlux)
            .expectSubscription()
            .expectNext("a", "c", "d", "a")
            .expectError()
            .verify()
    }

    @Test
    fun flatMapOperator() {
        val flux = Flux.just("a", "b")
        val flatFlux = flux.map { it.toUpperCase() }
//            .flatMap { findByName(it) }
            .flatMapSequential { findByName(it) } // Keep the order

        StepVerifier.create(flatFlux)
            .expectSubscription()
            .expectNext("nameA1", "nameA2", "nameB1", "nameB2")
            .verifyComplete()
    }

    @Test
    fun zipOperator() {
        val title = Flux.just("Grand blue", "Baki")
        val studio = Flux.just("Zero G", "TMS Entretainment")
        val episodes = Flux.just(12, 24)

        val animeFlux = Flux.zip(title, studio, episodes)
            .flatMap { Flux.just(Anime(it.t1, it.t2, it.t3)) }

        StepVerifier
            .create(animeFlux)
            .expectSubscription()
            .expectNext(
                Anime("Grand blue", "Zero G", 12),
                Anime("Baki", "TMS Entretainment", 24)
            )
            .verifyComplete()
    }

    @Test
    fun zipWithOperator() {
        val title = Flux.just("Grand blue", "Baki")
        val studio = Flux.just("Zero G", "TMS Entretainment")

        val animeFlux = title.zipWith(studio)
            .flatMap { Flux.just(Anime(it.t1, it.t2, 10)) }

        StepVerifier
            .create(animeFlux)
            .expectSubscription()
            .expectNext(
                Anime("Grand blue", "Zero G", 10),
                Anime("Baki", "TMS Entretainment", 10)
            )
            .verifyComplete()
    }

    private fun findByName(name: String): Flux<String> =
        if (name == "A") {
            Flux.just("nameA1", "nameA2").delayElements(Duration.ofMillis(100L))
        } else {
            Flux.just("nameB1", "nameB2")
        }

    private fun emptyFlux(): Flux<Any> =
        Flux.empty()
}

data class Anime(
    val title: String,
    val studio: String,
    val episodes: Int
)