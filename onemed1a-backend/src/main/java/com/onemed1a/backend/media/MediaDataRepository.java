package com.onemed1a.backend.media;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.onemed1a.backend.media.MediaData.MediaType;

import java.util.Optional;
import java.util.UUID;

public interface MediaDataRepository
        extends JpaRepository<MediaData, UUID>, JpaSpecificationExecutor<MediaData> {
    Optional<MediaData> findByTitleAndType(String title, MediaType mediaTypeEnum);
}
