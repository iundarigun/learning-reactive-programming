package br.com.devcave.coroutines.flow

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout

fun main() {
    runBlocking {
        secondFlow().collect { println("Flow $it") }
    }
}

fun secondFlow(): Flow<Int> =
    flowOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)