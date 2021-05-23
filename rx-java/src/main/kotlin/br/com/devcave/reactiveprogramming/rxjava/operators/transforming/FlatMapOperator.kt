package br.com.devcave.reactiveprogramming.rxjava.operators.transforming

import br.com.devcave.reactiveprogramming.rxjava.observer.CustomObserver
import br.com.devcave.reactiveprogramming.rxjava.util.positiveNumbers
import io.reactivex.Observable

fun main() {
    Observable.fromIterable(positiveNumbers(10))
        .flatMap { Observable.fromIterable(listOf(it, it)) }
        .subscribe(CustomObserver())
}