package org.project.artconnect.dao.impl;

import org.project.artconnect.model.Gallery;

import java.util.List;
import java.util.Optional;

public interface GalleryDao {
    Optional<Gallery> findById(Long id);

    List<Gallery> findAll();
}
