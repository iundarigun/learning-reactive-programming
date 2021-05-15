package br.com.devcave.reactiveprogramming.rxjava.observable

import br.com.devcave.reactiveprogramming.rxjava.observer.CustomObserver
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

fun main() {
    Observable.range(2, 10)
        .subscribe(CustomObserver())
}