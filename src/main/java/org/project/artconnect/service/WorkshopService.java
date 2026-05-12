package org.project.artconnect.service;

import org.project.artconnect.model.Booking;
import org.project.artconnect.model.CommunityMember;
import org.project.artconnect.model.Workshop;

import java.util.List;
import java.util.Optional;

public interface WorkshopService {
    List<Workshop> getAllWorkshops();

    Optional<Workshop> getWorkshopByTitle(String title);

    void bookWorkshop(Workshop workshop, CommunityMember member);

    List<Booking> getBookingsByMember(CommunityMember member);
}
