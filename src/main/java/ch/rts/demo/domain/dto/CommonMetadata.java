package ch.rts.demo.domain.dto;

import java.util.Optional;

public interface CommonMetadata {
    String externalId();

    String title();

    Optional<String> description();

}
