package br.com.devcave.reactive.configuration

import org.flywaydb.core.Flyway
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FlywayConfiguration(
    private val flywayProperties: FlywayProperties
) {
    @Bean(initMethod = "migrate")
    fun flyway(): Flyway {
        val config = Flyway
            .configure()
            .dataSource(
                "jdbc:${flywayProperties.url}",
                flywayProperties.username,
                flywayProperties.password
            )
        return Flyway(config)
    }
}