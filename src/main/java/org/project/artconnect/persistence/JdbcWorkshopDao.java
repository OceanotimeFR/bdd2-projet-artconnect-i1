package org.project.artconnect.persistence;

import org.project.artconnect.dao.impl.WorkshopDao;
import org.project.artconnect.model.Workshop;
import org.project.artconnect.config.DatabaseConfig;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcWorkshopDao implements WorkshopDao {
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
    }
    private Workshop mapWorkshop(ResultSet rs) throws SQLException {
        Workshop w = new Workshop();
        w.setTitle(rs.getString("title"));

        Timestamp ts = rs.getTimestamp("date_");
        if (ts != null) {
            w.setDate(ts.toLocalDateTime());
        }

        int artistId = rs.getInt("id_artist");
        if (artistId > 0) {
            try (PreparedStatement pstmt = rs.getStatement().getConnection().prepareStatement("SELECT * FROM Artist WHERE id_artist = ?")) {
                pstmt.setInt(1, artistId);
                try (ResultSet ars = pstmt.executeQuery()) {
                    if (ars.next()) {
                        org.project.artconnect.model.Artist a = new org.project.artconnect.model.Artist();
                        a.setName(ars.getString("name"));
                        a.setBio(ars.getString("bio"));
                        w.setInstructor(a);
                    }
                }
            }
        }

        w.setDurationMinutes(rs.getObject("durationMinutes", Integer.class));
        w.setMaxParticipants(rs.getObject("maxParticipants", Integer.class));
        w.setPrice(rs.getDouble("price"));
        w.setLocation(rs.getString("location"));
        w.setDescription(rs.getString("description"));
        w.setLevel(rs.getString("level"));
        return w;
    }
    @Override
    public Optional<Workshop> findById(Long id) {
        String sql = "SELECT * FROM Workshop WHERE id_workshop = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapWorkshop(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return Optional.empty();
    }
    @Override
    public List<Workshop> findAll() {
        List<Workshop> list = new ArrayList<>();
        String sql = "SELECT * FROM Workshop";
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapWorkshop(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
}
