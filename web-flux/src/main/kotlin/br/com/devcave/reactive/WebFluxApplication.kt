package br.com.devcave.reactive

import br.com.devcave.reactive.configuration.FlywayProperties
import kotlinx.coroutines.debug.CoroutinesBlockHoundIntegration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import reactor.blockhound.BlockHound
import reactor.blockhound.integration.BlockHoundIntegration

@SpringBootApplication
@EnableConfigurationProperties(FlywayProperties::class)
class WebFluxApplication {
    init {
        val javaUUIDintegration = BlockHoundIntegration { builder ->
            builder.allowBlockingCallsInside("java.util.UUID", "randomUUID")
        }
        BlockHound.install(CoroutinesBlockHoundIntegration(), javaUUIDintegration)
    }
}

fun main(args: Array<String>) {
    runApplication<WebFluxApplication>(*args)
}
