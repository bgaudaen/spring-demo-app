package ch.rts.demo.domain;

import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document
public record Video(
    String id,
    SourceId sourceId,
    @TextIndexed(weight = 3) String title,
    @TextIndexed String description,
//    Instant createdDate,
    SourceId collectionSourceId
) {
    public Video {
        Objects.requireNonNull(id);
        Objects.requireNonNull(title);
    }
}
