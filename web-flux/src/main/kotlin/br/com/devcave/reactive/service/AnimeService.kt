package br.com.devcave.reactive.service

import br.com.devcave.reactive.domain.Anime
import br.com.devcave.reactive.repository.AnimeRepository
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@Service
class AnimeService(
    private val animeRepository: AnimeRepository
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun findAll(): Flux<Anime> {
        logger.info("findAll on service")
        return animeRepository.findAll()
    }

    fun findByName(name: String): Mono<Anime> {
        return animeRepository.findByName(name)
            .switchIfEmpty {
                Mono.error { ResponseStatusException(HttpStatus.NOT_FOUND, "Anime not found") }
            }
    }

    fun findById(id: Long): Mono<Anime> {
        return animeRepository.findById(id)
            .switchIfEmpty {
                Mono.error { ResponseStatusException(HttpStatus.NOT_FOUND, "Anime not found") }
            }
    }

    fun save(anime: Anime): Mono<Anime> {
        return animeRepository.findByName(anime.name).flatMap {
            Mono.error<Anime> { ResponseStatusException(HttpStatus.BAD_REQUEST, "Anime already exists") }
        }.switchIfEmpty {
            animeRepository.save(anime.copy(id = 0))
        }
    }

    fun update(id: Long, anime: Anime): Mono<Void> {
        return findById(id).map { anime.copy(id = it.id) }
            .flatMap { toSave ->
                animeRepository.findByName(toSave.name)
                    .filter { it.id != toSave.id }
                    .flatMap {
                        Mono.error<Void> {
                            ResponseStatusException(HttpStatus.BAD_REQUEST, "Anime already exists")
                        }
                    }
                    .switchIfEmpty {
                        animeRepository.save(toSave)
                            .then()
                    }
            }
    }

    fun delete(id: Long): Mono<Void> =
        findById(id)
            .flatMap { animeRepository.delete(it) }

    @Transactional
    fun saveAll(animes: List<Anime>): Flux<Anime> {
        return Flux.fromStream(animes.stream())
            .flatMap { anime ->
                animeRepository.findByName(anime.name).flatMap {
                    Mono.error<Anime>{ ResponseStatusException(HttpStatus.BAD_REQUEST, "Anime already exists") }
                }.switchIfEmpty {
                    animeRepository.save(anime.copy(id = 0))
                }
            }
    }
}