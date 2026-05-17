package org.project.artconnect.persistence;
import org.project.artconnect.dao.impl.GalleryDao;
import org.project.artconnect.model.Gallery;
import org.project.artconnect.config.DatabaseConfig;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
public class JdbcGalleryDao implements GalleryDao {
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
    }
    private Gallery mapGallery(ResultSet rs) throws SQLException {
        Gallery g = new Gallery();
        g.setName(rs.getString("name"));
        g.setStreetNumber(rs.getObject("street_number", Integer.class));
        g.setStreetName(rs.getString("street_name"));
        g.setCity(rs.getString("city"));
        g.setZipCode(rs.getObject("zip_code", Integer.class));
        g.setCountry(rs.getString("country"));
        g.setOwnerName(rs.getString("ownerName"));
        g.setOpeningHours(rs.getString("openingHours"));
        g.setContactPhone(rs.getString("contactPhone"));
        g.setRating(rs.getDouble("rating"));
        g.setWebsite(rs.getString("website"));
        return g;
    }
    @Override
    public Optional<Gallery> findById(Long id) {
        String sql = "SELECT * FROM Gallery WHERE id_gallery = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapGallery(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return Optional.empty();
    }
    @Override
    public List<Gallery> findAll() {
        List<Gallery> list = new ArrayList<>();
        String sql = "SELECT * FROM Gallery";
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapGallery(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
}
