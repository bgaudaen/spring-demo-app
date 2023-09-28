package ch.rts.demo.repository;

import ch.rts.demo.domain.model.VideoMetadataEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface VideoMetadataRepository extends ReactiveCrudRepository<VideoMetadataEntity, String> {
    Mono<VideoMetadataEntity> findByInternalId(String internalId);
    Mono<VideoMetadataEntity> findByExternalId(String externalId);
}
