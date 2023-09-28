package ch.rts.demo.domain.dto;

import ch.rts.demo.domain.JsonModelAnnotations;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import org.springframework.data.mongodb.core.index.Indexed;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SuperBuilder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@ToString
@Jacksonized
@JsonModelAnnotations
@FieldNameConstants
public class VideoMetadata implements CommonMetadata {

    /**
     * Declare the builder in a way that we can chain builders through hierarchy
     *
     * @param <C> Generic dto class
     * @param <B> Generic builder class
     */
    public abstract static class VideoMetadataBuilder<C extends VideoMetadata, B extends VideoMetadata.VideoMetadataBuilder<C, B>> {
        @JsonIgnore
        public B fillValuesFrom(VideoMetadata instance) {
            $fillValuesFromInstanceIntoBuilder(instance, this);
            return self();
        }
    }

    public static VideoMetadata.VideoMetadataBuilder<?, ?> toBuilder(VideoMetadata data) {
        return VideoMetadata.builder().fillValuesFrom(data)
            .clearChapters()
            .chapters(
                data.chapters.stream()
                    .map(cm -> {
                        if (ChapterMetadata.class.equals(cm.getClass())) {
                            return cm;
                        } else {
                            // Sort of downcast (do expose subclass data)
                            return ChapterMetadata.toBuilder(cm).build();
                        }
                    })
                    .collect(Collectors.toList())
            );
    }

    @NonNull
    @Indexed(unique = true)
    private final String externalId;

    @NonNull
    private final String title;

    private final String description;

    @NonNull
    private final URL streamingUrl;

    @NonNull
    @Singular
    protected final List<? extends ChapterMetadata> chapters;

    @Override
    public final String externalId() {
        return externalId;
    }

    @Override
    public final String title() {
        return title;
    }

    @Override
    public final Optional<String> description() {
        return Optional.ofNullable(description);
    }

    public URL streamingUrl() {
        return streamingUrl;
    }

    public List<? extends ChapterMetadata> chapters() {
        return chapters;
    }

    /**
     * Make this class non-final to group fields in subclasses
     */
    public static class Fields {
        protected Fields() {
        }
    }
}
