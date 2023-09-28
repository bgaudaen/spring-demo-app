package ch.rts.demo.domain.dto;

import ch.rts.demo.domain.JsonModelAnnotations;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.beans.Transient;
import java.time.Duration;
import java.util.Optional;

@SuperBuilder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@ToString
@Jacksonized
@JsonModelAnnotations
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "@type",
    defaultImpl = ChapterMetadata.class
)
@JsonTypeName("dto")
@FieldNameConstants
public class ChapterMetadata implements CommonMetadata {

    /**
     * Declare the builder in a way that we can chain builders through hierarchy
     *
     * @param <C> Generic dto class
     * @param <B> Generic builder class
     */
    public abstract static class ChapterMetadataBuilder<C extends ChapterMetadata, B extends ChapterMetadataBuilder<C, B>> {
        @JsonIgnore
        public B fillValuesFrom(ChapterMetadata instance) {
            $fillValuesFromInstanceIntoBuilder(instance, this);
            return self();
        }
    }

    /**
     * Allow to actually initialize a builder from a child object
     *
     * @param data a member of the class hierarchy
     * @return a builder initialized with values from the parameter
     */
    public static ChapterMetadataBuilder<?, ?> toBuilder(ChapterMetadata data) {
        return ChapterMetadata.builder().fillValuesFrom(data);
    }

    @NonNull
    private final String externalId;

    @NonNull
    private final String title;

    private final String description;

    @NonNull
    private final Long startTimeCode;

    @NonNull
    private final Long endTimeCode;

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

    public final Long startTimeCode() {
        return startTimeCode;
    }

    public final Long endTimeCode() {
        return endTimeCode;
    }

    @Transient
    public final Duration duration() {
        return Duration.ofMillis(endTimeCode - startTimeCode);
    }

    /**
     * Make this class non-final to group fields in subclasses
     */
    public static class Fields {
        protected Fields() {
        }
    }
}
