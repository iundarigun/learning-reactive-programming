package br.com.devcave.reactive.repository

import br.com.devcave.reactive.domain.Anime
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface AnimeRepository : ReactiveCrudRepository<Anime, Long>