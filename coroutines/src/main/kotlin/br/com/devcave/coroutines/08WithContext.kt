package br.com.devcave.coroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.system.measureTimeMillis

fun main() {
    println(measureTimeMillis { withContextIO() })
}

suspend fun withContextIODelay(): Int {
    delay(2_000L)
    return 15
}

fun withContextIO() = runBlocking {
    val number1 = withContext(Dispatchers.IO) { asyncDelay() }
    val number2 = withContext(Dispatchers.IO) { asyncDelay() }
    val result = number1 + number2
    println(result)
}
