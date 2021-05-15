package br.com.devcave.reactiveprogramming.rxjava.observable

import br.com.devcave.reactiveprogramming.rxjava.observer.CustomObserver
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

fun main() {
    Observable.timer(1, TimeUnit.SECONDS)
        .subscribe(CustomObserver())
    Thread.sleep(4_000)
}