package br.com.devcave.reactive.integration

import br.com.devcave.reactive.configuration.FakerConfiguration
import br.com.devcave.reactive.configuration.FakerConfiguration.faker
import br.com.devcave.reactive.domain.Anime
import br.com.devcave.reactive.factory.AnimeFactory
import br.com.devcave.reactive.repository.AnimeRepository
import kotlinx.coroutines.debug.CoroutinesBlockHoundIntegration
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import org.springframework.web.reactive.function.BodyInserters
import reactor.blockhound.BlockHound
import reactor.core.scheduler.Schedulers
import java.util.concurrent.FutureTask
import java.util.concurrent.TimeUnit

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class AnimeControllerTest(
    private val testClient: WebTestClient,
    private val animeRepository: AnimeRepository
) {

    @AfterEach
    fun deleteDatabase() {
        animeRepository.deleteAll().subscribe()
    }

    @Test
    fun blockHoundWorks() {
        val result = runCatching {
            val task = FutureTask {
                Thread.sleep(0)
            }
            Schedulers.parallel().schedule(task)
            task.get(1, TimeUnit.SECONDS)
        }
        Assertions.assertTrue(result.isFailure)
    }

    @Test
    fun `listAll returns a flux of anime successfully`() {
        val buildList = AnimeFactory.buildList().map { animeRepository.save(it).block() }

        testClient
            .get()
            .uri("/animes")
            .exchange()
            .expectStatus()
            .is2xxSuccessful
            .expectBody()
            .jsonPath("$.[0].id").isEqualTo(requireNotNull(buildList[0]?.id))
            .jsonPath("$.[0].name").isEqualTo(requireNotNull(buildList[0]?.name))
    }

    @Test
    fun `listAll returns a flux of anime successfully V2`() {
        val buildList = AnimeFactory.buildList().map { animeRepository.save(it).block() }

        testClient
            .get()
            .uri("/animes")
            .exchange()
            .expectStatus()
            .is2xxSuccessful
            .expectBodyList(Anime::class.java)
            .hasSize(buildList.size)
            .contains(buildList[0])
    }

    @Test
    fun `findById returns an anime successfully`() {
        val anime = requireNotNull(AnimeFactory
            .buildList(1)
            .map { animeRepository.save(it).block() }
            .first())

        testClient
            .get()
            .uri("/animes/{id}", anime.id)
            .exchange()
            .expectStatus().isOk
            .expectBody<Anime>()
            .isEqualTo(anime)
    }

    @Test
    fun `findById returns an error when does not exist`() {
        val id = faker.number().numberBetween(10_000L, 100_000L)

        testClient
            .get()
            .uri("/animes/{id}", id)
            .exchange()
            .expectStatus().isNotFound
            .expectBody()
            .jsonPath("$.status").isEqualTo(404)
    }

    @Test
    fun `save creates an anime successfully`() {
        val anime = AnimeFactory.build(0)

        testClient
            .post()
            .uri("/animes/")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(anime))
            .exchange()
            .expectStatus().isCreated
            .expectBody()
            .jsonPath("$.name").isEqualTo(anime.name)
    }

    @Test
    fun `save returns bad request when name is empty`() {
        val anime = Anime(id = 0, name = "")

        testClient
            .post()
            .uri("/animes/")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(anime))
            .exchange()
            .expectStatus().isBadRequest
            .expectBody()
            .jsonPath("$.status").isEqualTo(400)
    }

    companion object {
        @BeforeAll
        @JvmStatic
        internal fun beforeAll() {
            BlockHound.install(CoroutinesBlockHoundIntegration())
        }
    }
}