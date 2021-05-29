package br.com.devcave.reactive.controller

import br.com.devcave.reactive.domain.Anime
import br.com.devcave.reactive.service.AnimeService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import javax.validation.Valid

@RestController
@RequestMapping("animes")
class AnimeController(
    private val animeService: AnimeService
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping
    fun getAll(): Flux<Anime> {
        logger.info("getAll")
        return animeService.findAll()
    }

    @GetMapping("name/{name}")
    fun findByName(@PathVariable name: String): Mono<Anime> {
        logger.info("findByName")
        return animeService.findByName(name)
    }

    @GetMapping("{id}")
    fun findById(@PathVariable id: Long): Mono<Anime> {
        logger.info("findById")
        return animeService.findById(id)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun save(@Valid @RequestBody anime: Anime): Mono<Anime> {
        return animeService.save(anime)
    }
    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun save(@PathVariable id: Long, @Valid @RequestBody anime: Anime): Mono<Void> {
        return animeService.update(id, anime)
    }
}