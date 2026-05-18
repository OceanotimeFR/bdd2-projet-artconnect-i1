package org.project.artconnect.service.impl;

import org.project.artconnect.model.Artwork;
import org.project.artconnect.model.CommunityMember;
import org.project.artconnect.model.Review;
import org.project.artconnect.service.ArtworkService;
import org.project.artconnect.service.CommunityService;

import java.util.*;

public class InMemoryCommunityService implements CommunityService {
    private final Map<String, CommunityMember> members = new LinkedHashMap<>();

    public InMemoryCommunityService() {
    }

    public void initData(ArtworkService artworkService) {
        CommunityMember alice = addMember("Alice Wonderland", "alice@art.com", "Paris");
        CommunityMember bob = addMember("Bob Ross", "bob@happytrees.com", "London");
        CommunityMember charlie = addMember("Charlie Brown", "charlie@peanuts.com", "New York");

        addReview(alice, artworkService.getArtworkByTitle("Mona Lisa").orElse(null), 5, "Unbelievable detail!");
        addReview(bob, artworkService.getArtworkByTitle("Water Lilies").orElse(null), 4, "The colors are stunning.");
        addReview(charlie, artworkService.getArtworkByTitle("The Thinker").orElse(null), 5, "Deeply moving.");
    }

    private CommunityMember addMember(String name, String email, String city) {
        CommunityMember m = new CommunityMember(name, email);
        m.setCity(city);
        m.setMembershipType(CommunityMember.MembershipType.PREMIUM);
        members.put(name, m);
        return m;
    }

    private void addReview(CommunityMember member, Artwork artwork, int rating, String comment) {
        if (member == null || artwork == null)
            return;
        Review r = new Review(member, artwork, rating, comment);
        member.getReviews().add(r);
    }

    @Override
    public List<CommunityMember> getAllMembers() {
        return new ArrayList<>(members.values());
    }

    @Override
    public Optional<CommunityMember> getMemberByName(String name) {
        return Optional.ofNullable(members.get(name));
    }

    @Override
    public List<Review> getReviewsByMember(CommunityMember member) {
        if (member == null)
            return Collections.emptyList();
        return member.getReviews();
    }

    @Override
    public void createMember(CommunityMember member) {

    }

    @Override
    public void updateMember(CommunityMember member) {

    }

    @Override
    public void deleteMember(String email) {

    }
}
