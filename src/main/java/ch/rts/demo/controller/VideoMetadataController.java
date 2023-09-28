package ch.rts.demo.controller;

import ch.rts.demo.domain.dto.VideoMetadata;
import ch.rts.demo.domain.model.VideoMetadataEntity;
import ch.rts.demo.repository.VideoMetadataRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("/video")
public class VideoMetadataController {

    private final VideoMetadataRepository repository;

    public VideoMetadataController(VideoMetadataRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public Mono<ResponseEntity<VideoMetadata>> create(@RequestBody VideoMetadata video) {
        return repository
            .save(VideoMetadataEntity.toBuilder(video).build())
            .map(entity ->
                ResponseEntity
                    .created(URI.create("/video/" + entity.internalId()))
                    .body(entity.toDTO())
            );
    }

    @GetMapping
    public Flux<VideoMetadataEntity> getAllVideos() {
        return repository.findAll();
    }

    @GetMapping("/{videoId}")
    public Mono<ResponseEntity<VideoMetadata>> getVideoById(@PathVariable String videoId) {
        return repository
            .findByInternalId(videoId)
            .map(VideoMetadataEntity::toDTO)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{videoId}")
    public Mono<ResponseEntity<VideoMetadata>> updateVideoById(@PathVariable String videoId, @RequestBody VideoMetadata video) {
        return repository
            .findByInternalId(videoId)
            .flatMap(it -> repository.save(it.merge(video)))
            .map(VideoMetadataEntity::toDTO)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{videoId}")
    public Mono<ResponseEntity<Void>> deleteVideoById(@PathVariable String videoId) {
        return repository.deleteById(videoId)
            .then(Mono.fromCallable(() -> ResponseEntity.ok().<Void>build()))
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
