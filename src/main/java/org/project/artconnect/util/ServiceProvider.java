package org.project.artconnect.util;

import org.project.artconnect.service.*;
import org.project.artconnect.service.impl.*;

/**
 * Service Provider to manage singleton instances of services and handle their
 * initialization.
 */
public class ServiceProvider {
    private static final ArtistService artistService = new DatabaseArtistService();
    private static final ArtworkService artworkService = new DatabaseArtworkService();
    private static final GalleryService galleryService = new DatabaseGalleryService();
    private static final WorkshopService workshopService = new DatabaseWorkshopService();
    private static final CommunityService communityService = new DatabaseCommunityService();

    static {
        // Init block no longer needs to use mocks since Services talk to Database directly
    }

    public static ArtistService getArtistService() {
        return artistService;
    }

    public static ArtworkService getArtworkService() {
        return artworkService;
    }

    public static GalleryService getGalleryService() {
        return galleryService;
    }

    public static WorkshopService getWorkshopService() {
        return workshopService;
    }

    public static CommunityService getCommunityService() {
        return communityService;
    }
}
