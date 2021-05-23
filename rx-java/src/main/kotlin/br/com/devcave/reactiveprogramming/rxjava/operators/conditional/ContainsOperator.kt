package br.com.devcave.reactiveprogramming.rxjava.operators.conditional

import br.com.devcave.reactiveprogramming.rxjava.util.positiveNumbers
import io.reactivex.Observable
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import org.slf4j.LoggerFactory

fun main() {
    val logger = LoggerFactory.getLogger("ContainsOperator")
    Observable.fromIterable(positiveNumbers(10))
        .contains(7)
        .subscribe(object : SingleObserver<Boolean> {
            override fun onSubscribe(d: Disposable) {
                logger.info("on subscribe")
            }

            override fun onSuccess(t: Boolean) {
                logger.info("Element is present: $t")
            }

            override fun onError(e: Throwable) {
                logger.info("on Error", e)
            }
        })
}