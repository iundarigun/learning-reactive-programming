package br.com.devcave.reactiveprogramming.rxjava.util

import br.com.devcave.reactiveprogramming.rxjava.domain.Shape
import kotlin.random.Random


/**
 * Generate List of positive numbers
 * @param n
 * @return
 */
fun positiveNumbers(n: Int): List<Int> {
    return (1..n).map { it }
}

/**
 * List of positive numbers
 * @param n
 * @return
 */
fun shapes(n: Int): List<Shape> {
    return (1..n).map {
        Shape(randomShape(), randomColor())
    }
}

/**
 * Geneate List of prime numbers
 * @param n
 * @return
 */
fun primeNumbers(n: Int): List<Int> {
    val primeNumbers = mutableListOf<Int>()

    var i = 1
    while (primeNumbers.size < n) {
        if (isPrime(i)) {
            primeNumbers.add(i)
        }
        i++
    }
    return primeNumbers
}

/**
 * Find if number is prime
 * @param n
 * @return the result in boolean
 */
fun isPrime(n: Int): Boolean {
    if (n <= 1) {
        return false
    }
    var i = 2
    while (i <= Math.sqrt(n.toDouble())) {
        if (n % i == 0) {
            return false
        }
        i++
    }
    return true
}

private fun randomColor(): String {
    val colors = arrayOf("blue", "green", "red", "yellow", "pink")
    return colors[Random.nextInt(getRandomNumberInRange(1, 4))]
}

private fun randomShape(): String {
    val shape = arrayOf("square", "triangle", "circle", "pentagon", "hexagon", "stars")
    return shape[Random.nextInt(getRandomNumberInRange(1, 6))]
}

private fun getRandomNumberInRange(min: Int, max: Int): Int {
    require(min < max) { "max must be greater than min" }
    return Random.nextInt(max - min + 1) + min
}
