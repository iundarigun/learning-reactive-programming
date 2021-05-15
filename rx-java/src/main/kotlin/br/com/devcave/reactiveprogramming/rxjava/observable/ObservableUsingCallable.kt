package br.com.devcave.reactiveprogramming.rxjava.observable

import br.com.devcave.reactiveprogramming.rxjava.observer.CustomObserver
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

fun main() {

    Observable.fromCallable {
        doSomething()
        "Hello"
    }.subscribe(CustomObserver())
}

fun doSomething(){
    Thread.sleep(2_000)
}