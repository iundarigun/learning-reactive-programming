package br.com.devcave.reactiveprogramming.rxjava.operators.filtering

import br.com.devcave.reactiveprogramming.rxjava.observer.CustomObserver
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

fun main() {
    val obs1 = Observable.interval(1, TimeUnit.SECONDS)
    val obs2 = Observable.interval(5, TimeUnit.SECONDS)
    obs1.takeUntil(obs2).subscribe(CustomObserver())
    // We do noting with second observable but
    // stop consume events when the second starts

    Thread.sleep(15_000)
}