package br.com.devcave.reactiveprogramming.rxjava.operators.filtering

import br.com.devcave.reactiveprogramming.rxjava.observer.CustomObserver
import io.reactivex.Observable

fun main() {

    Observable.just("a", "b", "c", "d")
        .skip(2)
        .subscribe(CustomObserver())

    Observable.just("a", "b", "c", "d")
        .skipLast(2)
        .subscribe(CustomObserver())
}