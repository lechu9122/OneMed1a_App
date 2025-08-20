package com.onemed1a.backend.media;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MediaDataRepository extends JpaRepository<MediaData, UUID> {




}
