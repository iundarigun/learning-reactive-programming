package br.com.devcave.reactiveprogramming.rxjava.operators.conditional

import br.com.devcave.reactiveprogramming.rxjava.util.positiveNumbers
import io.reactivex.Observable
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import org.slf4j.LoggerFactory

fun main() {
    val logger = LoggerFactory.getLogger("AllOperator")
    Observable.fromIterable(positiveNumbers(10))
        .all { it % 2 == 0 } // Apply condition on all events
        .subscribe(object : SingleObserver<Boolean> {
            override fun onSubscribe(d: Disposable) {
                logger.info("on subscribe")
            }

            override fun onSuccess(t: Boolean) {
                logger.info("Operator condition even number: $t")
            }

            override fun onError(e: Throwable) {
                logger.info("on Error", e)
            }
        })
}