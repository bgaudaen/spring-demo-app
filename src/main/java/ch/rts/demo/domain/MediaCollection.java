package ch.rts.demo.domain;

import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document("media_collection")
public record MediaCollection(
    String id,
    SourceId sourceId,
    @TextIndexed String title,
    String description
) {
    public MediaCollection {
        Objects.requireNonNull(id);
        Objects.requireNonNull(title);
    }
}
