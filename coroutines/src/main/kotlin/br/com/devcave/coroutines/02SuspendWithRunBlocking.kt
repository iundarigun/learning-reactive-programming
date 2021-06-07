package br.com.devcave.coroutines

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

fun main() {
    suspendUsingRunBlockingExample()
    suspendUsingRunBlockingInMethodDescriptionExample()
}

suspend fun delayCoroutine(message: String) {
    delay(4_000L)
    println("$message - thread ${Thread.currentThread().name}")
}

/**
 * RunBlocking blocks the main thread, not using another thread
 */
fun suspendUsingRunBlockingExample() {
    println("Task 1 - thread ${Thread.currentThread().name}")
    runBlocking {
        delayCoroutine("Task 2")
    }
    println("Task 3 - thread ${Thread.currentThread().name}")
}

/**
 * RunBlocking blocks the main thread, not using another thread
 */
fun suspendUsingRunBlockingInMethodDescriptionExample() = runBlocking {
    println("Task 1 - thread ${Thread.currentThread().name}")
    delayCoroutine("Task 2")
    println("Task 3 - thread ${Thread.currentThread().name}")
}
