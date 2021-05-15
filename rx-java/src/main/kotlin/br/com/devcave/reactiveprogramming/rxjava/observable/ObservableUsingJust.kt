package br.com.devcave.reactiveprogramming.rxjava.observable

import br.com.devcave.reactiveprogramming.rxjava.observer.CustomObserver
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.slf4j.LoggerFactory

fun main() {
    anonymousObserver()
    customObserver()
}

private fun anonymousObserver() {
    val logger = LoggerFactory.getLogger("ObservableUsingJust")
    Observable
        .just("a", "b", "c", "d", "e", "f", "g", "h", "i", "j")
        .subscribe(object : Observer<String> {
            override fun onSubscribe(d: Disposable) {
                logger.info("onSubscribe")
            }

            override fun onNext(t: String) {
                logger.info("onNext -> $t")
            }

            override fun onError(e: Throwable) {
                logger.warn("onError ${e.message}", e)
            }

            override fun onComplete() {
                logger.info("onComplete")
            }
        })
}

private fun customObserver() {
    Observable
        .just("1", "2", "3", "4", "5", "6", "7", "8", "9", "10")
        .subscribe(CustomObserver())
}