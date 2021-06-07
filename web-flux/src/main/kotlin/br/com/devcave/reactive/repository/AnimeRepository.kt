package br.com.devcave.reactive.repository

import br.com.devcave.reactive.domain.Anime
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface AnimeRepository : ReactiveCrudRepository<Anime, Long> {
    fun findBy(pageable: Pageable): Flux<Anime>

    fun findByName(name: String): Mono<Anime>
}