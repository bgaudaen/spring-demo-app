package ch.rts.demo.repository;

import ch.rts.demo.domain.MediaCollection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface MediaCollectionRepository extends ReactiveCrudRepository<MediaCollection, String>, ReactiveSortingRepository<MediaCollection, String> {

    Flux<MediaCollection> findBy(Query query, Pageable pageable);

    @org.springframework.data.mongodb.repository.Query("{$text: {$search: ?0}}")
    Flux<MediaCollection> search(String text, Pageable pageable);

    Flux<MediaCollection> findAllBy(Pageable pageable);

}
