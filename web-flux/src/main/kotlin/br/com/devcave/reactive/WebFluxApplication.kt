package br.com.devcave.reactive

import br.com.devcave.reactive.configuration.FlywayProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(FlywayProperties::class)
class WebFluxApplication

fun main(args: Array<String>) {
	runApplication<WebFluxApplication>(*args)
}
