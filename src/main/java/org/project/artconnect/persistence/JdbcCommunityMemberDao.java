package org.project.artconnect.persistence;
import org.project.artconnect.dao.impl.CommunityMemberDao;
import org.project.artconnect.model.CommunityMember;
import org.project.artconnect.config.DatabaseConfig;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
public class JdbcCommunityMemberDao implements CommunityMemberDao {
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
    }
    private CommunityMember mapMember(ResultSet rs) throws SQLException {
        CommunityMember m = new CommunityMember();
        m.setName(rs.getString("name"));
        m.setEmail(rs.getString("email"));
        m.setBirthYear(rs.getObject("birthYear", Integer.class));
        m.setPhone(rs.getString("phone"));

        String type = rs.getString("membershipType");
        if (type != null) {
            try {
                m.setMembershipType(CommunityMember.MembershipType.valueOf(type.toUpperCase()));
            } catch (IllegalArgumentException e) {
                m.setMembershipType(CommunityMember.MembershipType.FREE);
            }
        }
        return m;
    }
    @Override
    public Optional<CommunityMember> findById(Long id) {
        String sql = "SELECT * FROM CommunityMember WHERE id_member = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapMember(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return Optional.empty();
    }
    @Override
    public List<CommunityMember> findAll() {
        List<CommunityMember> list = new ArrayList<>();
        String sql = "SELECT * FROM CommunityMember";
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapMember(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public void save(CommunityMember member) {
        String sql = "INSERT INTO CommunityMember (name, email) VALUES (?, ?)";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, member.getName());
            pstmt.setString(2, member.getEmail());
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void update(CommunityMember member) {
        String sql = "UPDATE CommunityMember SET name = ? WHERE email = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, member.getName());
            pstmt.setString(2, member.getEmail());
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void delete(String email) {
        String sql = "DELETE FROM CommunityMember WHERE email = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
