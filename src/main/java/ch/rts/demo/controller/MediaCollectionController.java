package ch.rts.demo.controller;

import ch.rts.demo.domain.MediaCollection;
import ch.rts.demo.repository.MediaCollectionRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin(origins = "http://localhost:5500")
@RequestMapping("/api/media-collection")
public class MediaCollectionController {

    private final MediaCollectionRepository repository;

    public MediaCollectionController(MediaCollectionRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public Flux<MediaCollection> getAllMediaCollections(@RequestParam(required = false) String search, Pageable pageable) {

        if (search != null && !search.isBlank()) {
            TextCriteria criteria = TextCriteria.forLanguage("fr")
                .matchingAny(search);

            Query query = TextQuery.queryText(criteria)
                .sortByScore()
                .with(pageable);

            return repository.findBy(query, pageable);
        }

        return repository.findAllBy(pageable);
    }

    @GetMapping("/{mediaCollectionId}")
    public Mono<ResponseEntity<MediaCollection>> getMediaCollectionById(@PathVariable String mediaCollectionId) {
        return repository
            .findById(mediaCollectionId)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
