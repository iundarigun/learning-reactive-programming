package br.com.devcave.reactive.repository;

import br.com.devcave.reactive.domain.Anime;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface AnimeRepository extends ReactiveCrudRepository<Anime, Long> {
}
