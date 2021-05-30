package br.com.devcave.reactive.factory

import br.com.devcave.reactive.configuration.FakerConfiguration.faker
import br.com.devcave.reactive.domain.Anime

object AnimeFactory {

    fun build(id: Long = faker.number().numberBetween(10_000L, 100_000L)): Anime =
        Anime(
            id = id,
            name = faker.funnyName().name()
        )
}