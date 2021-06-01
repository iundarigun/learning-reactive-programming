package br.com.devcave.reactive.repository

import br.com.devcave.reactive.domain.Anime
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface AnimeRepository : CoroutineCrudRepository<Anime, Long> {
    suspend fun findByName(name: String): Anime

    suspend fun existsByName(name: String): Boolean

    suspend fun existsByNameAndIdNot(name: String, id: Long): Boolean
}