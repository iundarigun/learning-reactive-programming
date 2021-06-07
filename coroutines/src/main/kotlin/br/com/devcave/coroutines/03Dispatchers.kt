package br.com.devcave.coroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking

fun main() {
    dispatcher()
}

/**
 * Dispatchers indicates what thread or kind of thread we want use.
 * note Dispatchers.Main only for Andriod
 */
fun dispatcher() {
    runBlocking {
        println("Without specify: Execution thread ${Thread.currentThread().name}")
    }
    runBlocking(Dispatchers.Unconfined) {
        println("Unconfined: Execution thread ${Thread.currentThread().name}")
    }
    runBlocking(Dispatchers.Default) { // Intensive use of CPU
        println("Default: Execution thread ${Thread.currentThread().name}")
    }
    runBlocking(Dispatchers.IO) { // Access to database or API Rest
        println("ID: Execution thread ${Thread.currentThread().name}")
    }
    runBlocking(newSingleThreadContext("My Thread")) {
        println("Custom: Execution thread ${Thread.currentThread().name}")
    }
}