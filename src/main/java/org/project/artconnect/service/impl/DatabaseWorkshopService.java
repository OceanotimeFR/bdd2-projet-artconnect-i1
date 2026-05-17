package org.project.artconnect.service.impl;
import org.project.artconnect.dao.impl.WorkshopDao;
import org.project.artconnect.model.Booking;
import org.project.artconnect.model.CommunityMember;
import org.project.artconnect.model.Workshop;
import org.project.artconnect.persistence.JdbcWorkshopDao;
import org.project.artconnect.service.WorkshopService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
public class DatabaseWorkshopService implements WorkshopService {
    private final WorkshopDao workshopDao;
    public DatabaseWorkshopService() {
        this.workshopDao = new JdbcWorkshopDao();
    }
    @Override
    public List<Workshop> getAllWorkshops() {
        return workshopDao.findAll();
    }
    @Override
    public Optional<Workshop> getWorkshopByTitle(String title) {
        return workshopDao.findAll().stream()
                .filter(w -> w.getTitle().equalsIgnoreCase(title))
                .findFirst();
    }
    @Override
    public void bookWorkshop(Workshop workshop, CommunityMember member) {
        // DB impl
    }
    @Override
    public List<Booking> getBookingsByMember(CommunityMember member) {
        return new ArrayList<>();
    }
}
