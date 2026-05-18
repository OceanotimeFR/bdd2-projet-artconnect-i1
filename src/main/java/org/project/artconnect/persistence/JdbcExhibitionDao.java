package org.project.artconnect.persistence;

import org.project.artconnect.dao.impl.ExhibitionDao;
import org.project.artconnect.model.Exhibition;
import org.project.artconnect.model.Gallery;
import org.project.artconnect.config.DatabaseConfig;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JdbcExhibitionDao implements ExhibitionDao {

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
    }

    private Exhibition mapExhibition(ResultSet rs) throws SQLException {
        Exhibition e = new Exhibition();
        e.setId(rs.getInt("id_exhibition"));
        e.setTitle(rs.getString("title"));

        Date sd = rs.getDate("startDate");
        if (sd != null) e.setStartDate(sd.toLocalDate());

        Date ed = rs.getDate("endDate");
        if (ed != null) e.setEndDate(ed.toLocalDate());

        e.setDescription(rs.getString("description"));
        e.setCuratorName(rs.getString("curatorName"));
        e.setTheme(rs.getString("theme"));

        String galleryName = rs.getString("galleryName");
        if (galleryName != null) {
            Gallery g = new Gallery();
            g.setName(galleryName);
            e.setGallery(g);
        }

        return e;
    }

    @Override
    public List<Exhibition> findAll() {
        List<Exhibition> list = new ArrayList<>();
        String sql = "SELECT e.*, g.name AS galleryName FROM Exhibition e LEFT JOIN Gallery g ON e.id_gallery = g.id_gallery";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapExhibition(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void save(Exhibition exhibition) {
        String sql = "INSERT INTO Exhibition (title, startDate, endDate, description, curatorName, theme, id_gallery) " +
                     "VALUES (?, ?, ?, ?, ?, ?, (SELECT id_gallery FROM Gallery WHERE name = ?))";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, exhibition.getTitle());
            pstmt.setDate(2, exhibition.getStartDate() != null ? Date.valueOf(exhibition.getStartDate()) : null);
            pstmt.setDate(3, exhibition.getEndDate() != null ? Date.valueOf(exhibition.getEndDate()) : null);
            pstmt.setString(4, exhibition.getDescription());
            pstmt.setString(5, exhibition.getCuratorName());
            pstmt.setString(6, exhibition.getTheme());
            pstmt.setString(7, exhibition.getGallery() != null ? exhibition.getGallery().getName() : "");
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Exhibition exhibition) {
        String sql = "UPDATE Exhibition SET startDate = ?, endDate = ?, description = ?, curatorName = ?, theme = ?, " +
                     "id_gallery = (SELECT id_gallery FROM Gallery WHERE name = ?) WHERE title = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, exhibition.getStartDate() != null ? Date.valueOf(exhibition.getStartDate()) : null);
            pstmt.setDate(2, exhibition.getEndDate() != null ? Date.valueOf(exhibition.getEndDate()) : null);
            pstmt.setString(3, exhibition.getDescription());
            pstmt.setString(4, exhibition.getCuratorName());
            pstmt.setString(5, exhibition.getTheme());
            pstmt.setString(6, exhibition.getGallery() != null ? exhibition.getGallery().getName() : "");
            pstmt.setString(7, exhibition.getTitle());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String title) {
        String sql = "DELETE FROM Exhibition WHERE title = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Exhibition> findByGalleryName(String galleryName) {
        List<Exhibition> list = new ArrayList<>();
        String sql = "SELECT e.*, g.name AS galleryName FROM Exhibition e JOIN Gallery g ON e.id_gallery = g.id_gallery WHERE g.name = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, galleryName);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapExhibition(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}

