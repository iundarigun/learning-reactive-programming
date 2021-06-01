package br.com.devcave.reactive.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("flyway")
data class FlywayProperties(
    val url: String,
    val username: String,
    val password: String
)