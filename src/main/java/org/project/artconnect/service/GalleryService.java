package org.project.artconnect.service;

import org.project.artconnect.model.Exhibition;
import org.project.artconnect.model.Gallery;

import java.util.List;
import java.util.Optional;

public interface GalleryService {
    List<Gallery> getAllGalleries();

    Optional<Gallery> getGalleryByName(String name);

    List<Exhibition> getExhibitionsByGallery(Gallery gallery);

    List<Exhibition> getAllExhibitions();

    void createGallery(Gallery gallery);
    void updateGallery(Gallery gallery);
    void deleteGallery(String name);
}
