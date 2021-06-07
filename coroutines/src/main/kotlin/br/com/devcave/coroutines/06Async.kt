package br.com.devcave.coroutines

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun main() {
    println(measureTimeMillis { asyncAwait() })
}

suspend fun asyncDelay(): Int {
    delay(2_000L)
    return 15
}

fun asyncAwait() = runBlocking {
    val number1 = async { asyncDelay() }.await()
    val number2 = async { asyncDelay() }.await()
    val result = number1 + number2
    println(result)
}
