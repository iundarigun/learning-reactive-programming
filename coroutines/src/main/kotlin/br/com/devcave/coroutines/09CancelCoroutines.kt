package br.com.devcave.coroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.system.measureTimeMillis

fun main() {
    cancelCoroutine()
}

fun cancelCoroutine() {
    runBlocking {
        val job = launch {
            repeat(1000) {
                println("job $it")
                delay(500L)
            }
        }
        delay(1400)
        job.cancel()
    }
}

