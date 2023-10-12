package ch.rts.demo.controller;

import ch.rts.demo.domain.SourceId;
import ch.rts.demo.domain.Video;
import ch.rts.demo.repository.VideoRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin(origins = "http://localhost:5500")
@RequestMapping("/api/video")
public class VideoController {

    private final VideoRepository repository;

    public VideoController(VideoRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public Flux<Video> getAllVideos(@RequestParam(required = false) String search, @PageableDefault(size = 20) Pageable pageable) {
        if (search != null && !search.isBlank()) {
            return repository.search(search, pageable);
        }

        return repository.findAllBy(pageable);
    }

    @GetMapping("/by-collection/{source}/{id}")
    public Flux<Video> getAllVideosByCollectionId(@PathVariable String source,
                                                  @PathVariable String id,
                                                  @PageableDefault(size = 20) Pageable pageable) {
        SourceId sourceId = new SourceId(source, id);

        return repository.findAllByCollectionSourceId(sourceId, pageable);

    }

    @GetMapping("/{videoId}")
    public Mono<ResponseEntity<Video>> getVideoById(@PathVariable String videoId) {
        return repository
            .findById(videoId)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
