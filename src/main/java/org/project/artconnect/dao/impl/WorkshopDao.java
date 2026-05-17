package org.project.artconnect.dao.impl;

import org.project.artconnect.model.Workshop;

import java.util.List;
import java.util.Optional;

public interface WorkshopDao {
    Optional<Workshop> findById(Long id);

    List<Workshop> findAll();

    void save(Workshop workshop);
    void update(Workshop workshop);
    void delete(String title);
}
