package ch.rts.demo.controller;

import ch.rts.demo.MongoDBInitializer;
import ch.rts.demo.domain.dto.ChapterMetadata;
import ch.rts.demo.domain.dto.VideoMetadata;
import ch.rts.demo.domain.model.VideoMetadataEntity;
import ch.rts.demo.repository.VideoMetadataRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(initializers = MongoDBInitializer.class)
@AutoConfigureWebTestClient
@AutoConfigureWebClient
public class VideoMetadataControllerTest {

    private static final VideoMetadata video;

    static {
        try {
            video = VideoMetadata.builder()
                .externalId("1")
                .title("first video title")
                .description("first video description")
                .streamingUrl(new URL("https://videos.rts.ch/path/to/video.m3u8"))
                .build();
        }
        catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private static final ChapterMetadata chapter =
        ChapterMetadata.builder()
            .externalId("1")
            .title("chapter 1")
            .startTimeCode(0L)
            .endTimeCode(1L)
            .build();

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private VideoMetadataRepository repository;

    @Test
    public void sequential_video_update() {
        URI resourceURI = webTestClient.post().uri("/video")
            .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
            .body(Mono.just(video), VideoMetadata.class)
            .exchange()
            .expectStatus().isCreated()
            .expectBody(VideoMetadataEntity.class)
            .returnResult()
            .getResponseHeaders()
            .getLocation();

        VideoMetadata update1 = webTestClient.put().uri(resourceURI)
            .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
            .body(Mono.just(
                video.toBuilder()
                    .chapter(chapter)
                    .build()
            ), VideoMetadataEntity.class)
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectBody(VideoMetadata.class)
            .returnResult().getResponseBody();

        assertThat(update1.chapters(), hasSize(1));

        VideoMetadata update2 = webTestClient.put().uri(resourceURI)
            .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
            .body(Mono.just(
                update1.toBuilder()
                    .chapter(
                        chapter.toBuilder()
                            .externalId("2")
                            .build()
                    )
                    .build()
            ), VideoMetadataEntity.class)
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectBody(VideoMetadata.class)
            .returnResult().getResponseBody();

        assertThat(update2.chapters(), hasSize(2));

        VideoMetadata update3 = webTestClient.put().uri(resourceURI)
            .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
            .body(Mono.just(
                update2.toBuilder()
                    .clearChapters()
                    .chapter(
                        chapter.toBuilder()
                            .externalId("3")
                            .build()
                    )
                    .build()
            ), VideoMetadataEntity.class)
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectBody(VideoMetadata.class)
            .returnResult().getResponseBody();

        assertThat(update3.chapters(), hasSize(1));

        VideoMetadataEntity entity = repository.findByExternalId(video.externalId()).block();
        assertThat(entity.chapters(), hasSize(3));
        assertThat(entity.publishedChapters(), hasSize(1));
    }

    @Test
    public void parallel_update_sequence() {
        // 1. Create initial video without chapters
        // 2. Execute previous update sequence in parallel
        // Only one update should succeed, the two others should fail with HTTP error code: 412 Precondition Failed
    }
}
