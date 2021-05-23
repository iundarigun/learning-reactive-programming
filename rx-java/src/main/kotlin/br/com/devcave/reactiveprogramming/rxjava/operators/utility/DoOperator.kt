package br.com.devcave.reactiveprogramming.rxjava.operators.utility

import br.com.devcave.reactiveprogramming.rxjava.observer.CustomObserver
import br.com.devcave.reactiveprogramming.rxjava.util.shapes
import io.reactivex.Observable
import org.slf4j.LoggerFactory

fun main() {
    val logger = LoggerFactory.getLogger("doOperator")
    Observable.fromIterable(shapes(2))
        .doOnSubscribe { logger.info("Do on Subscribe") }
        .doOnEach { it -> logger.info("current value ${it.value}") }
        .doOnNext { logger.info("Shape is $it ") }
        .doOnComplete { logger.info("doing on complete") }
        .subscribe(CustomObserver())

    Thread.sleep(5_000)
}