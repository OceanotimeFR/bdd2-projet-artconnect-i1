package org.project.artconnect.service.impl;
import org.project.artconnect.dao.impl.CommunityMemberDao;
import org.project.artconnect.model.CommunityMember;
import org.project.artconnect.model.Review;
import org.project.artconnect.persistence.JdbcCommunityMemberDao;
import org.project.artconnect.service.CommunityService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
public class DatabaseCommunityService implements CommunityService {
    private final CommunityMemberDao memberDao;
    public DatabaseCommunityService() {
        this.memberDao = new JdbcCommunityMemberDao();
    }
    @Override
    public List<CommunityMember> getAllMembers() {
        return memberDao.findAll();
    }
    @Override
    public Optional<CommunityMember> getMemberByName(String name) {
        return memberDao.findAll().stream()
                .filter(m -> m.getName().equalsIgnoreCase(name))
                .findFirst();
    }
    @Override
    public List<Review> getReviewsByMember(CommunityMember member) {
        // Return empty for now as requested
        return new ArrayList<>();
    }
}
