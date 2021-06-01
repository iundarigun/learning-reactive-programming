package br.com.devcave.reactive.repository

import br.com.devcave.reactive.domain.User
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface UserRepository : CoroutineCrudRepository<User, Long> {
    suspend fun findByUsername(username: String): User
}