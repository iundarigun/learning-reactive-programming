package br.com.devcave.coroutines.flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        (1..6).asFlow()
            .onEach { delay(100) }
            .flatMapMerge {
                emitFlatMapMergeFlow(it)
            }
            .collect { println("$it ") }
    // Emit in order without respect the first order
    }
}

fun emitFlatMapMergeFlow(request: Int): Flow<String> = flow {
    emit("first $request")
    delay(100)
    emit("secound $request")
}