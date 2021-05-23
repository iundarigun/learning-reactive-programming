package br.com.devcave.reactiveprogramming.rxjava.operators.utility

import br.com.devcave.reactiveprogramming.rxjava.observer.CustomObserver
import br.com.devcave.reactiveprogramming.rxjava.util.shapes
import io.reactivex.Observable

fun main() {
    Observable.fromIterable(shapes(10))
        .timestamp()
        .subscribe(CustomObserver())

    Thread.sleep(5_000)
}