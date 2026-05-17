package org.project.artconnect.service.impl;
import org.project.artconnect.dao.impl.GalleryDao;
import org.project.artconnect.model.Exhibition;
import org.project.artconnect.model.Gallery;
import org.project.artconnect.persistence.JdbcExhibitionDao;
import org.project.artconnect.persistence.JdbcGalleryDao;
import org.project.artconnect.service.GalleryService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
public class DatabaseGalleryService implements GalleryService {
    private final GalleryDao galleryDao;
    private final JdbcExhibitionDao exhibitionDao;
    public DatabaseGalleryService() {
        this.galleryDao = new JdbcGalleryDao();
        this.exhibitionDao = new JdbcExhibitionDao();
    }
    @Override
    public List<Gallery> getAllGalleries() {
        return galleryDao.findAll();
    }
    @Override
    public Optional<Gallery> getGalleryByName(String name) {
        return galleryDao.findAll().stream()
            .filter(g -> g.getName().equalsIgnoreCase(name))
            .findFirst();
    }
    @Override
    public List<Exhibition> getExhibitionsByGallery(Gallery gallery) {
        if (gallery == null || gallery.getName() == null) {
            return new ArrayList<>();
        }
        return exhibitionDao.findByGalleryName(gallery.getName());
    }

    public List<Exhibition> getAllExhibitions() {
        return exhibitionDao.findAll();
    }
}
