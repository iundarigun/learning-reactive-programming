package br.com.devcave.reactiveprogramming.rxjava.operators.combining

import br.com.devcave.reactiveprogramming.rxjava.observer.CustomObserver
import io.reactivex.Observable

fun main() {
    val obs1 = Observable.just("a", "b", "c", "d")
    val obs2 = Observable.just("1", "2", "3")

    Observable.zip(obs1, obs2) { item1, item2 ->
        "$item1-$item2"
    }.subscribe(CustomObserver())
}