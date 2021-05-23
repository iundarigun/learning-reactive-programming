package br.com.devcave.reactiveprogramming.rxjava.operators.filtering

import io.reactivex.CompletableObserver
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import org.slf4j.LoggerFactory

fun main() {
    val logger = LoggerFactory.getLogger("IgnoreElementsOperator")

    Observable.just("a", "b", "c", "d")
        .ignoreElements()
        .subscribe(object : CompletableObserver {
            override fun onSubscribe(d: Disposable) {
                logger.info("onsubscribe")
            }

            override fun onError(e: Throwable) {
                logger.warn("onerror ${e.message}", e)
            }

            override fun onComplete() {
                logger.info("onComplete")
            }
        })
}