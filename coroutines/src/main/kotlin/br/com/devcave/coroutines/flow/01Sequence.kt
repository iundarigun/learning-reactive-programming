package br.com.devcave.coroutines.flow

fun main() {
    show()
}

fun show() {
    sequence().forEach { println(it) }
}

fun sequence(): Sequence<Int> = sequence {
    (1..10).forEach {
        Thread.sleep(1000)
        yield(it)
    }
}
