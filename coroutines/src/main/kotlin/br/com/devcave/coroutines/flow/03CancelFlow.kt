package br.com.devcave.coroutines.flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout

fun main() {
    runBlocking {
        withTimeout(2_500) {
            cancelFlow().collect { println("Flow $it") }
        }
        println("done")
    }
}

fun cancelFlow(): Flow<Int> = flow {
    (1..10).forEach {
        delay(1_000)
        emit(it * 2)
    }
}