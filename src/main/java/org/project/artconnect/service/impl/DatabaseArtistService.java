package org.project.artconnect.service.impl;

import org.project.artconnect.dao.impl.ArtistDao;
import org.project.artconnect.model.Artist;
import org.project.artconnect.model.Discipline;
import org.project.artconnect.persistence.JdbcArtistDao;
import org.project.artconnect.service.ArtistService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DatabaseArtistService implements ArtistService {

    private final ArtistDao artistDao;

    public DatabaseArtistService() {
        this.artistDao = new JdbcArtistDao();
    }

    @Override
    public List<Artist> getAllArtists() {
        return artistDao.findAll();
    }

    @Override
    public Optional<Artist> getArtistByName(String name) {
        return artistDao.findAll().stream()
                .filter(a -> a.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    @Override
    public void createArtist(Artist artist) {
        artistDao.save(artist);
    }

    @Override
    public void updateArtist(Artist artist) {
        artistDao.update(artist);
    }

    @Override
    public void deleteArtist(String name) {
        artistDao.delete(name);
    }

    @Override
    public List<Discipline> getAllDisciplines() {
        return artistDao.findAllDisciplines();
    }

    @Override
    public List<Artist> searchArtists(String query, String disciplineName, String city) {
        List<Artist> artists;
        if (city != null && !city.isEmpty()) {
            artists = artistDao.findByCity(city);
        } else {
            artists = getAllArtists();
        }

        if (disciplineName != null && !disciplineName.isEmpty()) {
            artists = artists.stream()
                    .filter(a -> a.getDisciplines().stream().anyMatch(d -> d.getName().equalsIgnoreCase(disciplineName)))
                    .collect(Collectors.toList());
        }

        if (query != null && !query.isEmpty()) {
            artists = artists.stream()
                    .filter(a -> a.getName().toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toList());
        }

        return artists;
    }
}
