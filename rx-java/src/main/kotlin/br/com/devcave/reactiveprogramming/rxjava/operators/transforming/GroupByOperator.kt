package br.com.devcave.reactiveprogramming.rxjava.operators.transforming

import br.com.devcave.reactiveprogramming.rxjava.domain.Shape
import br.com.devcave.reactiveprogramming.rxjava.observer.CustomObserver
import br.com.devcave.reactiveprogramming.rxjava.util.shapes
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.observables.GroupedObservable
import io.reactivex.schedulers.Schedulers
import org.slf4j.LoggerFactory

fun main() {
    val logger = LoggerFactory.getLogger("GroupByOperator")

    Observable.fromIterable(shapes(10))
        .groupBy { it.color }
        .observeOn(Schedulers.newThread()) // To see better the group by on log
        .subscribe(object : Observer<GroupedObservable<String, Shape>> {
            override fun onSubscribe(d: Disposable) {
                logger.info("on subscribe")
            }

            override fun onNext(t: GroupedObservable<String, Shape>) {
                logger.info("key ${t.key}")
                t.subscribe(CustomObserver())
            }

            override fun onError(e: Throwable) {
                logger.warn("on error", e)
            }

            override fun onComplete() {
                logger.info("on complete")
            }
        })
    Thread.sleep(1_000)
}