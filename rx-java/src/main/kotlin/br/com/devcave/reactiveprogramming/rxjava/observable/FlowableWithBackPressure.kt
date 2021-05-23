package br.com.devcave.reactiveprogramming.rxjava.observable

import br.com.devcave.reactiveprogramming.rxjava.util.positiveNumbers
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import org.slf4j.LoggerFactory
import java.util.concurrent.atomic.AtomicInteger

fun main() {
    val logger = LoggerFactory.getLogger("FlowableWithBackPressure")
    val positiveNumberEvents = Flowable.fromIterable(positiveNumbers(1_000_000))
        .repeat()
        .observeOn(Schedulers.newThread(), false, 5)
        .subscribeOn(Schedulers.newThread())
        .doOnNext {
            logger.info("emmiting int -> $it")
        }

    positiveNumberEvents.subscribe(object : Subscriber<Int> {

        private var subscription: Subscription? = null
        private val counter = AtomicInteger(0)

        override fun onSubscribe(s: Subscription) {
            logger.info("onSubscribe")
            this.subscription = s
            s.request(5)
        }

        override fun onNext(t: Int) {
            logger.info("onNext -> $t")
            Thread.sleep(100L)
            if (counter.incrementAndGet() % 5 == 0) {
                subscription?.request(5)
            }
        }

        override fun onError(t: Throwable) {
            logger.warn("onError ${t.message}", t)
        }

        override fun onComplete() {
            logger.info("onComplete")
        }
    })

    Thread.sleep(100_000)
}
