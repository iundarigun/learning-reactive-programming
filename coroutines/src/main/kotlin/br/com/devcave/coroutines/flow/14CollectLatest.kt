package br.com.devcave.coroutines.flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun main() {
    runBlocking {
        val time = measureTimeMillis {
            collectLatestFlow()
                // If arrives a new item before finish this process, the collect is stopped
                // and start a new for the arrived item
                .collectLatest {
                    println("start process $it")
                    delay(300)
                    println("finish $it")
                }
        }
        println("$time ms")
    }
}

fun collectLatestFlow(): Flow<Int> = flow {
    (1..10).forEach {
        delay(100)
        emit(it)
    }
}