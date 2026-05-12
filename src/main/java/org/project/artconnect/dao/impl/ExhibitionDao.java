package org.project.artconnect.dao.impl;

import org.project.artconnect.model.Exhibition;

import java.util.List;

public interface ExhibitionDao {
    List<Exhibition> findAll();

    void save(Exhibition exhibition);

    void update(Exhibition exhibition);

    void delete(String title);
}
