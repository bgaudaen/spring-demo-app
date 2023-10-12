package ch.rts.demo.repository;

import ch.rts.demo.domain.SourceId;
import ch.rts.demo.domain.Video;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface VideoRepository extends ReactiveCrudRepository<Video, String>, ReactiveSortingRepository<Video, String> {

    @org.springframework.data.mongodb.repository.Query("{$text: {$search: ?0}}")
    Flux<Video> search(String text, Pageable pageable);

    Flux<Video> findAllBy(Pageable pageable);

    Flux<Video> findAllByCollectionSourceId(SourceId sourceId, Pageable pageable);

}
