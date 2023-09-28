package ch.rts.demo.domain.model;

import ch.rts.demo.domain.JsonModelAnnotations;
import ch.rts.demo.domain.dto.ChapterMetadata;
import ch.rts.demo.domain.dto.VideoMetadata;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.net.URL;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Document(collection = VideoMetadataEntity.VIDEO_METADATA_COLLECTION)
@TypeAlias("VideoMetadata")
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Jacksonized
@JsonModelAnnotations
@FieldNameConstants
public class VideoMetadataEntity extends VideoMetadata {

    public static final String VIDEO_METADATA_COLLECTION = "video_metadata";

    public abstract static class VideoMetadataEntityBuilder<C extends VideoMetadataEntity, B extends VideoMetadataEntityBuilder<C, B>> extends VideoMetadata.VideoMetadataBuilder<C, B> {

        public VideoMetadataEntityBuilder() {
            internalId = UUID.randomUUID().toString();
            createdDate = Instant.now();
            state = PublicationState.PUBLISHED;
        }

        @Override
        public B chapters(Collection<? extends ChapterMetadata> chapters) {
            if (chapters == null) {
                throw new NullPointerException("chapters cannot be null");
            }

            return super.chapters(
                chapters.stream()
                    .map(chapter -> {
                        if (chapter instanceof ChapterMetadataEntity) {
                            return chapter;
                        }

                        return ChapterMetadataEntity.toBuilder(chapter).build();
                    })
                    .collect(Collectors.toList())
            );
        }
    }

    public static VideoMetadataEntityBuilder<?, ?> toBuilder(VideoMetadata instance) {
        return VideoMetadataEntity.builder().fillValuesFrom(instance);
    }

    public static class Fields extends VideoMetadata.Fields {
    }

    @PersistenceCreator
    public VideoMetadataEntity(@NonNull String externalId,
                               @NonNull String title,
                               String description,
                               @NonNull URL streamingUrl,
                               @NonNull List<? extends ChapterMetadata> chapters,
                               String _id,
                               @NonNull String internalId,
                               @NonNull Instant createdDate,
                               @NonNull PublicationState state) {
        super(externalId, title, description, streamingUrl, chapters);
        this._id = _id;
        this.internalId = internalId;
        this.createdDate = createdDate;
        this.state = state;
    }

    @Id
    private final String _id;

    @NonNull
    @Indexed(unique = true)
    private final String internalId;

    @NonNull
    private final Instant createdDate;

    @NonNull
    private final PublicationState state;

    public String internalId() {
        return internalId;
    }

    public Instant createdDate() {
        return createdDate;
    }

    public PublicationState state() {
        return state;
    }

    @Override
    public List<ChapterMetadataEntity> chapters() {
        return chapters.stream().map(ChapterMetadataEntity.class::cast).collect(Collectors.toList());
    }

    public List<ChapterMetadataEntity> publishedChapters() {
        return chapters.stream().map(ChapterMetadataEntity.class::cast).filter(c -> c.state().equals(PublicationState.PUBLISHED)).collect(Collectors.toList());
    }

    public VideoMetadataEntity merge(VideoMetadata incoming) {
        // TODO: implement merge logic.
        return VideoMetadataEntity.toBuilder(incoming).build();
    }

    public VideoMetadata toDTO() {
        return VideoMetadata.builder().fillValuesFrom(this)
            .clearChapters()
            .chapters(
                chapters.stream()
                    .filter(chapter -> ((ChapterMetadataEntity) chapter).state().equals(PublicationState.PUBLISHED))
                    .map(cm -> ChapterMetadata.toBuilder(cm).build())
                    .collect(Collectors.toList())
            )
            .build();
    }
}
