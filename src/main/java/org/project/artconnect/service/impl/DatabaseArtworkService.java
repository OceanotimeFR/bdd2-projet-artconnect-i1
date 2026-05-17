package org.project.artconnect.service.impl;

import org.project.artconnect.dao.impl.ArtworkDao;
import org.project.artconnect.model.Artist;
import org.project.artconnect.model.Artwork;
import org.project.artconnect.persistence.JdbcArtworkDao;
import org.project.artconnect.service.ArtworkService;

import java.util.List;
import java.util.Optional;

public class DatabaseArtworkService implements ArtworkService {

    private final ArtworkDao artworkDao;

    public DatabaseArtworkService() {
        this.artworkDao = new JdbcArtworkDao();
    }

    @Override
    public List<Artwork> getAllArtworks() {
        return artworkDao.findAll();
    }

    @Override
    public Optional<Artwork> getArtworkByTitle(String title) {
        return artworkDao.findAll().stream()
                .filter(w -> w.getTitle().equalsIgnoreCase(title))
                .findFirst();
    }

    @Override
    public List<Artwork> getArtworksByArtist(Artist artist) {
        return artworkDao.findByArtistName(artist.getName());
    }

    @Override
    public void createArtwork(Artwork artwork) {
        artworkDao.save(artwork);
    }

    @Override
    public void updateArtwork(Artwork artwork) {
        artworkDao.update(artwork);
    }

    @Override
    public void deleteArtwork(String title) {
        artworkDao.delete(title);
    }
}

