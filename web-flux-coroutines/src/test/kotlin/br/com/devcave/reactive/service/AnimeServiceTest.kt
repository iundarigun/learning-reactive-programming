package br.com.devcave.reactive.service

import br.com.devcave.reactive.domain.Anime
import br.com.devcave.reactive.factory.AnimeFactory
import br.com.devcave.reactive.repository.AnimeRepository
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.debug.CoroutinesBlockHoundIntegration
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.asFlux
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.web.server.ResponseStatusException
import reactor.blockhound.BlockHound
import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers
import reactor.test.StepVerifier
import java.util.concurrent.FutureTask
import java.util.concurrent.TimeUnit

internal class AnimeServiceTest {
    private val animeRepository = mockk<AnimeRepository>()
    private val animeService = AnimeService(animeRepository)

    @AfterEach
    fun setup() {
        clearAllMocks()
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
    fun `findAll returns a flux of anime`() {
        val anime = AnimeFactory.build()

        every {
            animeRepository.findAll()
        } returns Flux.just(anime).asFlow()
        runBlocking {
            StepVerifier.create(animeService.findAll().asFlux())
                .expectSubscription()
                .expectNext(anime)
                .verifyComplete()
        }
        verify(exactly = 1) {
            animeRepository.findAll()
        }
    }

    @Test
    fun `save batch animes successfully`() {
        val anime = AnimeFactory.build(0)
        val otherAnime = AnimeFactory.build(0)
        val request = listOf(anime, otherAnime)

        coEvery {
            animeRepository.existsByName(any())
        } returns false

        coEvery {
            animeRepository.save(any())
        } answers { invocation.args[0] as Anime }

        runBlocking {
            StepVerifier.create(animeService.saveAll(request).asFlux())
                .expectSubscription()
                .expectNext(anime)
                .expectNext(otherAnime)
                .verifyComplete()
        }

        coVerify(exactly = 2) {
            animeRepository.existsByName(any())
        }
        coVerify(exactly = 2) {
            animeRepository.save(any())
        }
    }

    @Test
    fun `save batch an anime that already exists`() {
        val anime = AnimeFactory.build(0)
        val otherAnime = AnimeFactory.build(0)
        val request = listOf(anime, otherAnime)

        coEvery {
            animeRepository.existsByName(anime.name)
        } returns false

        coEvery {
            animeRepository.existsByName(otherAnime.name)
        } returns true

        coEvery {
            animeRepository.save(any())
        } answers { invocation.args[0] as Anime }

        runBlocking {
            StepVerifier.create(animeService.saveAll(request).asFlux())
                .expectSubscription()
                .expectNext(anime)
                .expectError(ResponseStatusException::class.java)
                .verify()
        }
        coVerify(exactly = 2) {
            animeRepository.existsByName(any())
        }
        coVerify(exactly = 1) {
            animeRepository.save(any())
        }
    }

    companion object {
        @BeforeAll
        @JvmStatic
        internal fun beforeAll() {
            BlockHound.install(CoroutinesBlockHoundIntegration())
        }
    }
}