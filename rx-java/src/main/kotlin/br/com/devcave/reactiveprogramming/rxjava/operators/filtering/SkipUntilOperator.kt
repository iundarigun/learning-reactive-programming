package br.com.devcave.reactiveprogramming.rxjava.operators.filtering

import br.com.devcave.reactiveprogramming.rxjava.observer.CustomObserver
import br.com.devcave.reactiveprogramming.rxjava.util.positiveNumbers
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

fun main() {
    val obs1 = Observable.interval(1, TimeUnit.SECONDS)
    val obs2 = Observable.interval(5, TimeUnit.SECONDS)
    obs1.skipUntil(obs2).subscribe(CustomObserver())
    // We do noting with second observable but
    // wait until start the second to get events emit for the first

    Thread.sleep(15_000)
}