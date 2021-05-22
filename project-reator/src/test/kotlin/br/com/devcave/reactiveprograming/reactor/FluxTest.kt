package br.com.devcave.reactiveprograming.reactor

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import org.slf4j.LoggerFactory
import reactor.blockhound.BlockHound
import reactor.core.publisher.BaseSubscriber
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.time.Duration

class FluxTest {

    private val logger = LoggerFactory.getLogger(javaClass)

    companion object {
        @BeforeAll
        @JvmStatic
        internal fun setUp() {
            BlockHound.install()
        }
    }

    @Test
    fun fluxSubscriber() {
        val fluxString = Flux.just("iunda", "devcave", "plotka", "laia")
            .log()

        StepVerifier.create(fluxString)
            .expectNext("iunda", "devcave", "plotka", "laia")
            .verifyComplete()
    }

    @Test
    fun fluxSubscriberNumbers() {
        val flux = Flux.range(1, 5)
            .log()

        flux.subscribe { logger.info("Number $it") }

        logger.info("----------")

        StepVerifier.create(flux)
            .expectNext(1, 2, 3, 4, 5)
            .verifyComplete()
    }

    @Test
    fun fluxSubscriberFromList() {
        val flux = Flux.fromIterable(listOf(1, 2, 3, 4, 5))
            .log()

        flux.subscribe { logger.info("Number $it") }

        logger.info("----------")

        StepVerifier.create(flux)
            .expectNext(1, 2, 3, 4, 5)
            .verifyComplete()
    }

    @Test
    fun fluxSubscriberNumberError() {
        val flux = Flux.range(1, 5)
            .log()
            .map {
                if (it == 4) {
                    throw IndexOutOfBoundsException("index error")
                }
                it
            }

        flux.subscribe(
            { logger.info("Number $it") },
            { logger.error("Error", it) },
            { logger.info("DONE") },
            { it.request(3) }
        )

        logger.info("----------")

        StepVerifier.create(flux)
            .expectNext(1, 2, 3)
            .expectError(IndexOutOfBoundsException::class.java)
            .verify()
    }

    @Test
    fun fluxSubscriberNumberUglyBackpressure() {
        val flux = Flux.range(1, 10)
            .log()

        flux.subscribe(object : Subscriber<Int> {

            private var subscription: Subscription? = null
            private var count = 0
            private val requestNumber = 2L

            override fun onSubscribe(subscription: Subscription) {
                this.subscription = subscription
                subscription.request(requestNumber)
            }

            override fun onNext(t: Int?) {
                count++
                if (count >= requestNumber) {
                    count = 0
                    subscription?.request(requestNumber)
                }
            }

            override fun onError(t: Throwable) {
                logger.error("Error", t)
            }

            override fun onComplete() {
                logger.info("onComplete!")
            }
        }
        )

        logger.info("----------")

        StepVerifier.create(flux)
            .expectNext(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
            .verifyComplete()
    }

    @Test
    fun fluxSubscriberNumberNotSoUglyBackpressure() {
        val flux = Flux.range(1, 10)
            .log()

        flux.subscribe(object : BaseSubscriber<Int>() {
            private var count = 0
            private val requestNumber = 2L

            override fun hookOnSubscribe(subscription: Subscription) {
                request(requestNumber)
            }

            override fun hookOnNext(value: Int) {
                count++
                if (count >= requestNumber) {
                    count = 0
                    request(requestNumber)
                }
            }
        }
        )

        logger.info("----------")

        StepVerifier.create(flux)
            .expectNext(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
            .verifyComplete()
    }

    @Test
    fun fluxSubscriberPrettyBackpressure() {
        val flux = Flux.range(1, 10)
            .log()
            .limitRate(3)

        flux.subscribe { logger.info("Number $it") }

        logger.info("----------")

        StepVerifier.create(flux)
            .expectNext(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
            .verifyComplete()
    }

    @Test
    fun fluxSubscriberIntervalOne() {
        val interval = Flux.interval(Duration.ofMillis(100))
            .take(10)
            .log()

        interval.subscribe {
            logger.info("number $it")
        }

        Thread.sleep(1_000)
    }

    @Test
    fun fluxSubscriberIntervalTwo() {
        StepVerifier.withVirtualTime { createIntervalOneDayFlux() }
            .expectSubscription()
            /* Can use this (wait 2 days)
            .thenAwait(Duration.ofDays(2))
            .expectNext(0L)
            .expectNext(1L)
            */
            /*Or can use this (wait a day before each) */
            .expectNoEvent(Duration.ofDays(1))
            .thenAwait(Duration.ofDays(1))
            .expectNext(0L)
            .thenAwait(Duration.ofDays(1))
            .expectNext(1L)
            .thenCancel()
            .verify()
    }

    private fun createIntervalOneDayFlux() = Flux.interval(Duration.ofDays(1))
        .take(10)
        .log()

    @Test
    fun connectableFlux() {
        val connectableFlux = Flux.range(1, 10)
            .delayElements(Duration.ofMillis(100))
            .publish()
/*
        connectableFlux.connect()

        logger.info("Sleep for 300 ms")

        Thread.sleep(300)

        connectableFlux.subscribe { logger.info("subs1 number $it") }

        logger.info("sleep for 200ms")

        Thread.sleep(200)
        connectableFlux.subscribe { logger.info("subs2 number $it") }
*/

        StepVerifier
            .create(connectableFlux)
            .then { connectableFlux.connect() }
            .thenConsumeWhile { it <= 5 }
            .expectNext(6, 7, 8, 9, 10)
            .expectComplete()
            .verify()
    }

    @Test
    fun connectableFluxAutoConnect() {
        val connectableFlux = Flux.range(1, 5)
            .log()
            .delayElements(Duration.ofMillis(100))
            .publish()
            .autoConnect(2)

        StepVerifier
            .create(connectableFlux)
            .then { connectableFlux.subscribe() } // This is need because we define minimum subscribers to start publish
            .expectNext(1, 2, 3, 4, 5)
            .expectComplete()
            .verify()
    }
}