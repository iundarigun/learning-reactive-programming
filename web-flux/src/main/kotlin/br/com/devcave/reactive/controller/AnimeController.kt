package br.com.devcave.reactive.controller

import br.com.devcave.reactive.domain.Anime
import br.com.devcave.reactive.repository.AnimeRepository
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
@RequestMapping("anime")
class AnimeController(
    private val animeRepository: AnimeRepository
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping
    fun getAll(): Flux<Anime> {
        return animeRepository.findAll()
    }

}