package br.com.devcave.coroutines

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun main() {
    println(measureTimeMillis { asyncAwaitDeferred() })
}

suspend fun asyncDeferredDelay(): Int {
    delay(2_000L)
    return 15
}

fun asyncAwaitDeferred() = runBlocking {
    val deferred1 = async { asyncDelay() }
    val deferred2 = async { asyncDelay() }
    val result = deferred1.await() + deferred2.await()
    println(result)
}
