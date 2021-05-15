package br.com.devcave.reactiveprogramming.rxjava.observable

import br.com.devcave.reactiveprogramming.rxjava.observer.CustomObserver
import br.com.devcave.reactiveprogramming.rxjava.util.positiveNumbers
import br.com.devcave.reactiveprogramming.rxjava.util.shapes
import io.reactivex.Observable

fun main() {
    val observableDefer = Observable.defer<Any> {
        Observable.fromIterable(positiveNumbers(5))
    }
    observableDefer.subscribe(CustomObserver())
    observableDefer.subscribe(CustomObserver())
}