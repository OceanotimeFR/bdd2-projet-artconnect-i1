package org.project.artconnect.dao.impl;

import org.project.artconnect.model.Artist;
import org.project.artconnect.model.Discipline;
import java.util.List;

public interface ArtistDao {
    List<Artist> findAll();
    void save(Artist artist);
    void update(Artist artist);
    void delete(String artistName);
    List<Artist> findByCity(String city);
    List<Discipline> findAllDisciplines();
}
