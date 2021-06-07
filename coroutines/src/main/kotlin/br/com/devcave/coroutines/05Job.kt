package br.com.devcave.coroutines

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    job()
    Thread.sleep(4_500)
}

suspend fun jobDelayCoroutine(message: String) {
    delay(4_000L)
    println("$message - thread ${Thread.currentThread().name}")
}

fun job() {
    println("Task 1 - thread ${Thread.currentThread().name}")
    val job = GlobalScope.launch {
        jobDelayCoroutine("Task 2")
    }
    println("Task 3 - thread ${Thread.currentThread().name}")
    job.cancel()
}
