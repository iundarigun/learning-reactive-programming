package br.com.devcave.coroutines.flow

import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.reduce
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        val list = (1..10).asFlow().toList()
        println("list $list")
        val first = (1..10).asFlow().first()
        println("first $first")
        val reduce = (1..10).asFlow().reduce { it1, it2 -> it1 + it2 }
        println("reduce $reduce")
    }
}