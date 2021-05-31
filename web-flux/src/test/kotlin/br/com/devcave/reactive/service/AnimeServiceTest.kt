package br.com.devcave.reactive.service

import br.com.devcave.reactive.configuration.FakerConfiguration.faker
import br.com.devcave.reactive.domain.Anime
import br.com.devcave.reactive.factory.AnimeFactory
import br.com.devcave.reactive.repository.AnimeRepository
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.debug.CoroutinesBlockHoundIntegration
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.web.server.ResponseStatusException
import reactor.blockhound.BlockHound
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
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
        } returns Flux.just(anime)

        StepVerifier.create(animeService.findAll())
            .expectSubscription()
            .expectNext(anime)
            .verifyComplete()

        verify(exactly = 1) {
            animeRepository.findAll()
        }
    }

    @Test
    fun `findById returns Mono with anime when it exists`() {
        val anime = AnimeFactory.build()

        every {
            animeRepository.findById(anime.id)
        } returns Mono.just(anime)

        StepVerifier.create(animeService.findById(anime.id))
            .expectSubscription()
            .expectNext(anime)
            .verifyComplete()

        verify(exactly = 1) {
            animeRepository.findById(anime.id)
        }
    }

    @Test
    fun `findById returns Mono error with anime does not exist`() {
        val anime = AnimeFactory.build()

        every {
            animeRepository.findById(anime.id)
        } returns Mono.empty()

        StepVerifier.create(animeService.findById(anime.id))
            .expectSubscription()
            .expectError(ResponseStatusException::class.java)
            .verify()

        verify(exactly = 1) {
            animeRepository.findById(anime.id)
        }
    }

    @Test
    fun `save creates an anime when successfully`() {
        val anime = AnimeFactory.build()
        val request = anime.copy(id = 0)

        every {
            animeRepository.save(request)
        } returns Mono.just(anime)

        every {
            animeRepository.findByName(request.name)
        } returns Mono.empty()

        StepVerifier.create(animeService.save(request))
            .expectSubscription()
            .expectNext(anime)
            .verifyComplete()

        verify(exactly = 1) {
            animeRepository.save(request)
            animeRepository.findByName(request.name)
        }
    }

    @Test
    fun `save creates an anime already exists`() {
        val anime = AnimeFactory.build()
        val request = anime.copy(id = 0)

        every {
            animeRepository.findByName(request.name)
        } returns Mono.just(anime)

        StepVerifier.create(animeService.save(request))
            .expectSubscription()
            .expectError(ResponseStatusException::class.java)
            .verify()

        verify(exactly = 1) {
            animeRepository.findByName(request.name)
        }
        verify(exactly = 0) {
            animeRepository.save(request)
        }
    }

    @Test
    fun `delete removes the anime successfully`() {
        val anime = AnimeFactory.build()
        every {
            animeRepository.delete(anime)
        } returns Mono.empty()

        every {
            animeRepository.findById(anime.id)
        } returns Mono.just(anime)

        StepVerifier.create(animeService.delete(anime.id))
            .expectSubscription()
            .verifyComplete()

        verify(exactly = 1) {
            animeRepository.findById(anime.id)
            animeRepository.delete(anime)
        }
    }

    @Test
    fun `delete returns Mono error when anime does not exist`() {
        val id = faker.number().numberBetween(10_000L, 100_000)

        every {
            animeRepository.findById(id)
        } returns Mono.empty()

        StepVerifier.create(animeService.delete(id))
            .expectSubscription()
            .expectError(ResponseStatusException::class.java)
            .verify()

        verify(exactly = 1) {
            animeRepository.findById(id)
        }
        verify(exactly = 0) {
            animeRepository.delete(any())
        }
    }

    @Test
    fun `save update an anime without changes when successfully`() {
        val anime = AnimeFactory.build()
        val request = anime.copy()

        every {
            animeRepository.findById(anime.id)
        } returns Mono.just(anime)

        every {
            animeRepository.findByName(request.name)
        } returns Mono.just(anime)

        every {
            animeRepository.save(request)
        } returns Mono.just(anime)

        StepVerifier.create(animeService.update(anime.id, request))
            .expectSubscription()
            .verifyComplete()

        verify(exactly = 1) {
            animeRepository.save(request)
            animeRepository.findByName(request.name)
            animeRepository.findById(anime.id)
        }
    }

    @Test
    fun `save update an anime changing name when successfully`() {
        val anime = AnimeFactory.build()
        val request = anime.copy(name = faker.funnyName().name())

        every {
            animeRepository.findById(anime.id)
        } returns Mono.just(anime)

        every {
            animeRepository.findByName(request.name)
        } returns Mono.empty()

        every {
            animeRepository.save(request)
        } returns Mono.just(anime)

        StepVerifier.create(animeService.update(anime.id, request))
            .expectSubscription()
            .verifyComplete()

        verify(exactly = 1) {
            animeRepository.save(request)
            animeRepository.findByName(request.name)
            animeRepository.findById(anime.id)
        }
    }

    @Test
    fun `save update an anime does not exists`() {
        val anime = AnimeFactory.build()
        val request = anime.copy(name = faker.funnyName().name())

        every {
            animeRepository.findById(anime.id)
        } returns Mono.empty()

        StepVerifier.create(animeService.update(anime.id, request))
            .expectSubscription()
            .expectError(ResponseStatusException::class.java)
            .verify()

        verify(exactly = 1) {
            animeRepository.findById(anime.id)
        }
        verify(exactly = 0) {
            animeRepository.save(any())
            animeRepository.findByName(any())
        }
    }

    @Test
    fun `save update an anime that already exists`() {
        val anime = AnimeFactory.build()
        val otherAnime = AnimeFactory.build()
        val request = anime.copy(name = otherAnime.name)

        every {
            animeRepository.findById(anime.id)
        } returns Mono.just(anime)

        every {
            animeRepository.findByName(request.name)
        } returns Mono.just(otherAnime)

        StepVerifier.create(animeService.update(anime.id, request))
            .expectSubscription()
            .expectError(ResponseStatusException::class.java)
            .verify()

        verify(exactly = 1) {
            animeRepository.findById(anime.id)
            animeRepository.findByName(request.name)
        }
        verify(exactly = 0) {
            animeRepository.save(any())
        }
    }

    @Test
    fun `save batch animes successfully`() {
        val anime = AnimeFactory.build(0)
        val otherAnime = AnimeFactory.build(0)
        val request = listOf(anime, otherAnime)

        every {
            animeRepository.findByName(any())
        } returns Mono.empty()

        every {
            animeRepository.save(any())
        } answers { Mono.just(invocation.args[0] as Anime) }

        StepVerifier.create(animeService.saveAll(request))
            .expectSubscription()
            .expectNext(anime)
            .expectNext(otherAnime)
            .verifyComplete()

        verify(exactly = 2) {
            animeRepository.findByName(any())
        }
        verify(exactly = 2) {
            animeRepository.save(any())
        }
    }

    @Test
    fun `save batch an anime that already exists`() {
        val anime = AnimeFactory.build(0)
        val otherAnime = AnimeFactory.build(0)
        val request = listOf(anime, otherAnime)

        every {
            animeRepository.findByName(anime.name)
        } returns Mono.empty()

        every {
            animeRepository.findByName(otherAnime.name)
        } returns Mono.just(AnimeFactory.build())

        every {
            animeRepository.save(any())
        } answers { Mono.just(invocation.args[0] as Anime) }

        StepVerifier.create(animeService.saveAll(request))
            .expectSubscription()
            .expectNext(anime)
            .expectError(ResponseStatusException::class.java)
            .verify()

        verify(exactly = 2) {
            animeRepository.findByName(any())
        }
        verify(exactly = 1) {
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