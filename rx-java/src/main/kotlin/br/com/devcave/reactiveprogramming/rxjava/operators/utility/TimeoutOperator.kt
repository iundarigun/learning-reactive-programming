package br.com.devcave.reactiveprogramming.rxjava.operators.utility

import br.com.devcave.reactiveprogramming.rxjava.observer.CustomObserver
import io.reactivex.Observable
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

fun main() {
    Observable.timer(2, TimeUnit.SECONDS)
        .timeout(1, TimeUnit.SECONDS) // When pass one seconds before next event, throws an error
        .subscribe(CustomObserver())

    Thread.sleep(5_000)
}