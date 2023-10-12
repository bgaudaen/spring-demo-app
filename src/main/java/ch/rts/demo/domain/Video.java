package ch.rts.demo.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Objects;

@Document
public record Video(
    String id,
    SourceId sourceId,
    String title,
    String description,
//    Instant createdDate,
    SourceId collectionSourceId
) {
    public Video {
        Objects.requireNonNull(id);
        Objects.requireNonNull(title);
    }
}
