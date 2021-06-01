package br.com.devcave.reactive.controller

import br.com.devcave.reactive.domain.Anime
import br.com.devcave.reactive.service.AnimeService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.security.SecurityScheme
import kotlinx.coroutines.flow.Flow
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
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
@SecurityScheme(
    name = "BasicAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "basic"
)
class AnimeController(
    private val animeService: AnimeService
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping
    suspend fun getAll(): Flow<Anime> {
        logger.info("getAll")
        return animeService.findAll()
    }

    @GetMapping("name/{name}")
    suspend fun findByName(@PathVariable name: String): Anime {
        logger.info("findByName")
        return animeService.findByName(name)
    }

    @GetMapping("{id}")
    suspend fun findById(@PathVariable id: Long): Anime {
        logger.info("findById")
        return animeService.findById(id)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun save(@Valid @RequestBody anime: Anime): Anime {
        return animeService.save(anime)
    }

    @PostMapping("batch")
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun saveBatch(@RequestBody animes: List<Anime>): Flow<Anime> {
        return animeService.saveAll(animes)
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    suspend fun update(@PathVariable id: Long, @Valid @RequestBody anime: Anime) {
        return animeService.update(id, anime)
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    suspend fun delete(@PathVariable id: Long) {
        return animeService.delete(id)
    }
}