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

        Integer duration = null;
        int durationValue = rs.getInt("durationMinutes");
        if (!rs.wasNull()) {
            duration = durationValue;
        }
        w.setDurationMinutes(duration != null ? duration : 0);

        Integer maxParticipants = null;
        int maxValue = rs.getInt("maxParticipants");
        if (!rs.wasNull()) {
            maxParticipants = maxValue;
        }
        w.setMaxParticipants(maxParticipants != null ? maxParticipants : 0);
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

    @Override
    public void save(Workshop workshop) {
        String selectArtistSql = "SELECT id_artist FROM Artist WHERE name = ?";
        String sql = "INSERT INTO Workshop (title, date_, level, price, id_artist) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection()) {
            Integer artistId = null;
            if (workshop.getInstructor() != null && workshop.getInstructor().getName() != null) {
                try (PreparedStatement pstmt = conn.prepareStatement(selectArtistSql)) {
                    pstmt.setString(1, workshop.getInstructor().getName());
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            artistId = rs.getInt(1);
                        }
                    }
                }
            }
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, workshop.getTitle());
                if (workshop.getDate() != null) {
                    pstmt.setTimestamp(2, Timestamp.valueOf(workshop.getDate()));
                } else {
                    pstmt.setTimestamp(2, Timestamp.valueOf(java.time.LocalDateTime.now()));
                }
                pstmt.setString(3, workshop.getLevel());
                pstmt.setDouble(4, workshop.getPrice());
                if (artistId != null) {
                    pstmt.setInt(5, artistId);
                } else {
                    pstmt.setNull(5, Types.INTEGER);
                }
                pstmt.executeUpdate();
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void update(Workshop workshop) {
        String selectArtistSql = "SELECT id_artist FROM Artist WHERE name = ?";
        String sql = "UPDATE Workshop SET date_ = ?, level = ?, price = ?, id_artist = ? WHERE title = ?";
        try (Connection conn = getConnection()) {
            Integer artistId = null;
            if (workshop.getInstructor() != null && workshop.getInstructor().getName() != null) {
                try (PreparedStatement pstmt = conn.prepareStatement(selectArtistSql)) {
                    pstmt.setString(1, workshop.getInstructor().getName());
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            artistId = rs.getInt(1);
                        }
                    }
                }
            }
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                if (workshop.getDate() != null) {
                    pstmt.setTimestamp(1, Timestamp.valueOf(workshop.getDate()));
                } else {
                    pstmt.setTimestamp(1, Timestamp.valueOf(java.time.LocalDateTime.now()));
                }
                pstmt.setString(2, workshop.getLevel());
                pstmt.setDouble(3, workshop.getPrice());
                if (artistId != null) {
                    pstmt.setInt(4, artistId);
                } else {
                    pstmt.setNull(4, Types.INTEGER);
                }
                pstmt.setString(5, workshop.getTitle());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void delete(String title) {
        String sql = "DELETE FROM Workshop WHERE title = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
