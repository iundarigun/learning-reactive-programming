package br.com.devcave.reactive.configuration

import com.github.javafaker.Faker
import org.slf4j.LoggerFactory
import java.util.Random

object FakerConfiguration {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val seed = System.currentTimeMillis()
    val faker = Faker(Random(seed)).also { logger.info("FAKER SEED **** $seed ****") }
}
