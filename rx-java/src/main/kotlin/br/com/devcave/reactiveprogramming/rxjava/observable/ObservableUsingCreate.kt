package br.com.devcave.reactiveprogramming.rxjava.observable

import br.com.devcave.reactiveprogramming.rxjava.observer.CustomObserver
import br.com.devcave.reactiveprogramming.rxjava.util.shapes
import io.reactivex.Observable

fun main() {
    val shapes = shapes(5)
    Observable.create<Any> { observableEmitter ->
        runCatching {
            shapes.forEach { observableEmitter.onNext(it) }
        }.onFailure { observableEmitter.onError(it) }
        observableEmitter.onComplete()
    }.subscribe(CustomObserver())
}