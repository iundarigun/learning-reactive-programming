package br.com.devcave.coroutines.flow

import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.reduce
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        (1..10).asFlow()
            .filter {
                println("filter $it")
                it %2 != 0
            }
            .map {
                println("map $it")
                it*10
            }
            .collect {
                println("collect $it")
            }
    }
}