package br.com.devcave.coroutines.flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        launch { (1..10).forEach {
            delay((1_000))
            println("not blocked: $it")
            }
        }
        runFirstFlow().collect { println("Flow $it") }
    }
}

fun runFirstFlow(): Flow<Int> = flow {
    (1..10).forEach {
        delay(1_000)
        emit(it * 2)
    }
}