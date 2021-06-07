package br.com.devcave.coroutines

fun main() {
    blockingExample()
}

fun longTaskWithMessage(message: String ){
    Thread.sleep(4_000L)
    println("$message - thread ${Thread.currentThread().name}")
}

fun blockingExample(){
    println("Task 1 - thread ${Thread.currentThread().name}")
    longTaskWithMessage("Task 2")
    println("Task 3 - thread ${Thread.currentThread().name}")
}