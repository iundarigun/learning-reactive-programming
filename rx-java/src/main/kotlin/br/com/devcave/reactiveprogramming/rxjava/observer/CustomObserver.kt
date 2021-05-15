package br.com.devcave.reactiveprogramming.rxjava.observer

import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.slf4j.LoggerFactory

class CustomObserver : Observer<Any> {
    private val logger = LoggerFactory.getLogger(javaClass)
    override fun onSubscribe(d: Disposable) {
        logger.info("onSubscribe")
    }

    override fun onNext(t: Any) {
        logger.info("onNext -> $t")
    }

    override fun onError(e: Throwable) {
        logger.warn("onError ${e.message}", e)
    }

    override fun onComplete() {
        logger.info("onComplete")
    }
}