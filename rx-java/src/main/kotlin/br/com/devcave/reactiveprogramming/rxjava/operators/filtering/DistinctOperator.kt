package br.com.devcave.reactiveprogramming.rxjava.operators.filtering

import br.com.devcave.reactiveprogramming.rxjava.observer.CustomObserver
import io.reactivex.Observable

fun main() {
    Observable.just(1, 2, 3, 2, 4, 1, 2, 3, 4, 5)
        .map {
            Thread.sleep(500)
            it
        }
        .distinct()
        .subscribe(CustomObserver())
}