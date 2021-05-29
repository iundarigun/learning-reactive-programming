package br.com.devcave.reactive.repository

import br.com.devcave.reactive.domain.Anime
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface AnimeRepository : ReactiveCrudRepository<Anime, Long> {
    fun findByName(name: String): Mono<Anime>
}