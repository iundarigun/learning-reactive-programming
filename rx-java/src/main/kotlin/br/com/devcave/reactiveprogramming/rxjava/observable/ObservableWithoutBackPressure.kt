package br.com.devcave.reactiveprogramming.rxjava.observable

import br.com.devcave.reactiveprogramming.rxjava.observer.CustomObserver
import br.com.devcave.reactiveprogramming.rxjava.util.positiveNumbers
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

fun main() {

    val positiveNumberEvents = Observable.fromIterable(positiveNumbers(1_000_000))
        .repeat()
        .observeOn(Schedulers.newThread())
        .subscribeOn(Schedulers.newThread())

    positiveNumberEvents.subscribe(CustomObserver())

    Thread.sleep(100_000)
}
