package ch.rts.demo.controller;

import ch.rts.demo.MongoDBInitializer;
import ch.rts.demo.domain.Video;
import ch.rts.demo.repository.VideoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(initializers = MongoDBInitializer.class)
@AutoConfigureWebTestClient
@AutoConfigureWebClient
public class VideoMetadataControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private VideoRepository repository;

    @BeforeEach
    public void init() {
        repository.deleteAll().block();

//        Flux.range(1, 10)
//            .map(id -> new Video(
//                String.valueOf(id),
//                "title " + id,
//                "description " + id,
//                null
//            ))
//            .flatMap(repository::save)
//            .blockLast();
    }

    @Test
    public void sequential_video_update() {

//        URI resourceURI = webTestClient.post().uri("/video")
//            .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
//            .body(Mono.just(video), VideoMetadata.class)
//            .exchange()
//            .expectStatus().isCreated()
//            .expectBody(VideoMetadataEntity.class)
//            .returnResult()
//            .getResponseHeaders()
//            .getLocation();
//
//        VideoMetadata update1 = webTestClient.put().uri(resourceURI)
//            .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
//            .body(Mono.just(
//                video.toBuilder()
//                    .chapter(chapter)
//                    .build()
//            ), VideoMetadataEntity.class)
//            .exchange()
//            .expectStatus().is2xxSuccessful()
//            .expectBody(VideoMetadata.class)
//            .returnResult().getResponseBody();
//
//        assertThat(update1.chapters(), hasSize(1));
//
//        VideoMetadata update2 = webTestClient.put().uri(resourceURI)
//            .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
//            .body(Mono.just(
//                update1.toBuilder()
//                    .chapter(
//                        chapter.toBuilder()
//                            .externalId("2")
//                            .build()
//                    )
//                    .build()
//            ), VideoMetadataEntity.class)
//            .exchange()
//            .expectStatus().is2xxSuccessful()
//            .expectBody(VideoMetadata.class)
//            .returnResult().getResponseBody();
//
//        assertThat(update2.chapters(), hasSize(2));
//
//        VideoMetadata update3 = webTestClient.put().uri(resourceURI)
//            .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
//            .body(Mono.just(
//                update2.toBuilder()
//                    .clearChapters()
//                    .chapter(
//                        chapter.toBuilder()
//                            .externalId("3")
//                            .build()
//                    )
//                    .build()
//            ), VideoMetadataEntity.class)
//            .exchange()
//            .expectStatus().is2xxSuccessful()
//            .expectBody(VideoMetadata.class)
//            .returnResult().getResponseBody();
//
//        assertThat(update3.chapters(), hasSize(1));
//
//        VideoMetadataEntity entity = repository.findByExternalId(video.externalId()).block();
//        assertThat(entity.chapters(), hasSize(3));
//        assertThat(entity.publishedChapters(), hasSize(1));
    }

    @Test
    public void parallel_update_sequence() {
        // 1. Create initial video without chapters
        // 2. Execute previous update sequence in parallel
        // Only one update should succeed, the two others should fail with HTTP error code: 412 Precondition Failed
    }
}
