package br.com.devcave.reactiveprogramming.rxjava.operators.filtering

import io.reactivex.MaybeObserver
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import org.slf4j.LoggerFactory

fun main() {
    val logger = LoggerFactory.getLogger("ElementAtObservable")

    Observable.just("a", "b", "c", "d")
        .elementAt(2)
//        .elementAt(4)
        .subscribe(object : MaybeObserver<String> {
            override fun onSubscribe(d: Disposable) {
                logger.info("onsubscribe")
            }

            override fun onSuccess(t: String) {
                logger.info("onsuccess $t")
            }

            override fun onError(e: Throwable) {
                logger.warn("onerror ${e.message}", e)
            }

            override fun onComplete() {
                logger.info("onComplete")
            }
        })
}