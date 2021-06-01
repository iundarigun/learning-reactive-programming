package br.com.devcave.reactive.repository

import br.com.devcave.reactive.domain.User
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface UserRepository : CoroutineCrudRepository<User, Long> {
    suspend fun findByUsername(username: String): User
}