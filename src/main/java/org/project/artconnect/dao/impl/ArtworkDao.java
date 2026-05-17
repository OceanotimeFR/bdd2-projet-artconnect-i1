package org.project.artconnect.dao.impl;

import org.project.artconnect.model.Artwork;
import java.util.List;

/**
 * Data Access Object for Artwork entity.
 */
public interface ArtworkDao {
    List<Artwork> findAll();

    void save(Artwork artwork);

    void update(Artwork artwork);

    void delete(String title);

    List<Artwork> findByArtistName(String artistName);
}
