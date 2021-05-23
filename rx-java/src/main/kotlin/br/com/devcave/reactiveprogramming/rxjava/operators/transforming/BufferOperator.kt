package br.com.devcave.reactiveprogramming.rxjava.operators.transforming

import br.com.devcave.reactiveprogramming.rxjava.observer.CustomObserver
import br.com.devcave.reactiveprogramming.rxjava.util.shapes
import io.reactivex.Observable

fun main() {
    Observable.fromIterable(shapes(10))
        .buffer(3)
        .subscribe(CustomObserver())
}