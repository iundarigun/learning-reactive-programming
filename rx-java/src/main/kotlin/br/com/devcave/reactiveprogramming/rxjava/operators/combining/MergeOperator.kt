package br.com.devcave.reactiveprogramming.rxjava.operators.combining

import br.com.devcave.reactiveprogramming.rxjava.observer.CustomObserver
import io.reactivex.Observable

fun main() {
    val obs1 = Observable.just("a", "b", "c", "d")
    val obs2 = Observable.just("1", "2", "3")

    Observable.merge(obs1, obs2)
        .subscribe(CustomObserver())
}