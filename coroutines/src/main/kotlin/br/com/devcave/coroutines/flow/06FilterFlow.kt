package br.com.devcave.coroutines.flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        (1..10).asFlow()
            .filter { it % 2  == 0 }
            .map { filterPerformRequest(it) }
            .collect{ println(it) }
    }
}

suspend fun filterPerformRequest(request: Int) : String {
    delay(1000)
    return "response $request"
}