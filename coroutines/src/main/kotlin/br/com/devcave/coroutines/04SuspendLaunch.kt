package br.com.devcave.coroutines

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    launch()
    Thread.sleep(4_500)
}

suspend fun launchDelayCoroutine(message: String) {
    delay(4_000L)
    println("$message - thread ${Thread.currentThread().name}")
}

fun launch() {
    println("Task 1 - thread ${Thread.currentThread().name}")
    GlobalScope.launch {
        launchDelayCoroutine("Task 2")
    }
    println("Task 3 - thread ${Thread.currentThread().name}")
}
