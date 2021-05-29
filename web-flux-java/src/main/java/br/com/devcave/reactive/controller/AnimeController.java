package br.com.devcave.reactive.controller;

import br.com.devcave.reactive.AnimeService;
import br.com.devcave.reactive.domain.Anime;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping("animes")
public class AnimeController {
    private final AnimeService animeService;

    @GetMapping
    public Flux<Anime> getAll(){
        return animeService.findAll();
    }
}
