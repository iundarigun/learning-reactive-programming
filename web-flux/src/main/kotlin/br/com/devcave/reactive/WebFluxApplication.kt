package br.com.devcave.reactive

import br.com.devcave.reactive.configuration.FlywayProperties
import kotlinx.coroutines.debug.CoroutinesBlockHoundIntegration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import reactor.blockhound.BlockHound

@SpringBootApplication
@EnableConfigurationProperties(FlywayProperties::class)
class WebFluxApplication {
    init {
        BlockHound.install(CoroutinesBlockHoundIntegration())
    }
}

fun main(args: Array<String>) {
    runApplication<WebFluxApplication>(*args)
}
