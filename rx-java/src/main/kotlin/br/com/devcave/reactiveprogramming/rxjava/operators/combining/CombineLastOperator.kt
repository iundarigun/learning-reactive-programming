package br.com.devcave.reactiveprogramming.rxjava.operators.combining

import br.com.devcave.reactiveprogramming.rxjava.observer.CustomObserver
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

fun main() {

    val obs1 = Observable.interval(1000, TimeUnit.MILLISECONDS)
    val obs2 = Observable.interval(1500, TimeUnit.MILLISECONDS)

    Observable.combineLatest(obs1, obs2) { item1, item2 ->
        "first $item1, second $item2"
    }.subscribe(CustomObserver())

    Thread.sleep(10_000)
}