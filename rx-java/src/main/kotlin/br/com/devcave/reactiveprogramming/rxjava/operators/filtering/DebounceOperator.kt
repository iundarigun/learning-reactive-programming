package br.com.devcave.reactiveprogramming.rxjava.operators.filtering

import br.com.devcave.reactiveprogramming.rxjava.observer.CustomObserver
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit
import kotlin.random.Random

fun main() {
    val logger = LoggerFactory.getLogger("DebounceOperator")
    val random = Random(System.currentTimeMillis())
    val list = mutableListOf<Char>()
    Observable.interval(2, TimeUnit.SECONDS)
        .map {
            list.apply { this.add((random.nextInt(26) + 'a'.toInt()).toChar()) }
        }
        .doOnEach { logger.info("Current value: $it") }
        .debounce(2, TimeUnit.SECONDS)
        .subscribeOn(Schedulers.newThread())
        .observeOn(Schedulers.newThread())
        .subscribe(CustomObserver())

    Thread.sleep(10_000L)
}