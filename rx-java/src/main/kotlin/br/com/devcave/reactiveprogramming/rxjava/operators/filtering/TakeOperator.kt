package br.com.devcave.reactiveprogramming.rxjava.operators.filtering

import br.com.devcave.reactiveprogramming.rxjava.observer.CustomObserver
import br.com.devcave.reactiveprogramming.rxjava.util.positiveNumbers
import io.reactivex.Observable

fun main() {

    Observable.fromIterable(positiveNumbers(10))
        .take(2)
        .subscribe(CustomObserver())

    Observable.fromIterable(positiveNumbers(10))
        .takeLast(2)
        .subscribe(CustomObserver())
}