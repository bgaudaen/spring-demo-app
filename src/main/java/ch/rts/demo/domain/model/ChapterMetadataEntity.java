package ch.rts.demo.domain.model;

import ch.rts.demo.domain.JsonModelAnnotations;
import ch.rts.demo.domain.dto.ChapterMetadata;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import org.springframework.data.annotation.PersistenceCreator;

import java.util.UUID;

@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Jacksonized
@JsonModelAnnotations
@JsonTypeName("entity")
public class ChapterMetadataEntity extends ChapterMetadata {

    /**
     * {@inheritDoc}
     */
    public abstract static class ChapterMetadataEntityBuilder<C extends ChapterMetadataEntity, B extends ChapterMetadataEntity.ChapterMetadataEntityBuilder<C, B>> extends ChapterMetadata.ChapterMetadataBuilder<C, B> {
        public ChapterMetadataEntityBuilder() {
            internalId = UUID.randomUUID().toString();
            state = PublicationState.PUBLISHED;
        }
    }

    @PersistenceCreator
    public ChapterMetadataEntity(@NonNull String externalId, @NonNull String title, String description, @NonNull Long startTimeCode, @NonNull Long endTimeCode, @NonNull String internalId, @NonNull PublicationState state) {
        super(externalId, title, description, startTimeCode, endTimeCode);
        this.internalId = internalId;
        this.state = state;
    }

    @NonNull
    private final String internalId;

    @NonNull
    private final PublicationState state;

    public String internalId() {
        return internalId;
    }

    public PublicationState state() {
        return state;
    }

    public static ChapterMetadataEntityBuilder<?, ?> toBuilder(ChapterMetadata instance) {
        return ChapterMetadataEntity.builder().fillValuesFrom(instance);
    }

    public static class Fields extends ChapterMetadata.Fields {
    }

    public ChapterMetadataEntity merge(ChapterMetadata instance) {
        return toBuilder().fillValuesFrom(instance)
            .internalId(internalId())
            .state(state())
            .build();
    }
}
