package br.com.devcave.reactive.service

import br.com.devcave.reactive.domain.Anime
import br.com.devcave.reactive.repository.AnimeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asFlow
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Flux

@Service
class AnimeService(
    private val animeRepository: AnimeRepository
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    suspend fun findAll(): Flow<Anime> {
        logger.info("findAll on service")
        return animeRepository.findAll()
    }

    @Transactional(readOnly = true)
    suspend fun findByName(name: String): Anime =
        animeRepository.findByName(name) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Anime not found")

    @Transactional(readOnly = true)
    suspend fun findById(id: Long): Anime {
        return animeRepository.findById(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Anime not found")
    }

    @Transactional
    suspend fun save(anime: Anime): Anime {
        if (animeRepository.existsByName(anime.name)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Anime already exists")
        }
        return animeRepository.save(anime.copy(id = 0))
    }

    @Transactional
    suspend fun update(id: Long, anime: Anime) {
        if (animeRepository.existsById(id)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Anime not found")
        }
        if (animeRepository.existsByNameAndIdNot(anime.name, id)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Anime already exists")
        }

        animeRepository.save(anime.copy(id = id))
    }

    @Transactional
    suspend fun delete(id: Long) {
        if (animeRepository.existsById(id)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Anime not found")
        }
        animeRepository.deleteById(id)
    }

    @Transactional
    suspend fun saveAll(animes: List<Anime>): Flow<Anime> {
        return Flux.fromIterable(animes).asFlow()
            .map { anime ->
                if (animeRepository.existsByName(anime.name)) {
                    throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Anime already exists")
                }
                animeRepository.save(anime.copy(id = 0))
            }
    }
}