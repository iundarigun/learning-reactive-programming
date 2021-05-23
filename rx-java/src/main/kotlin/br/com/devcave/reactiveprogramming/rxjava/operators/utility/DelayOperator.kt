package br.com.devcave.reactiveprogramming.rxjava.operators.utility

import br.com.devcave.reactiveprogramming.rxjava.observer.CustomObserver
import br.com.devcave.reactiveprogramming.rxjava.util.positiveNumbers
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

fun main() {
    Observable.fromIterable(positiveNumbers(10))
        .delay(2, TimeUnit.SECONDS)
        .subscribe(CustomObserver())

    Thread.sleep(5_000)
}