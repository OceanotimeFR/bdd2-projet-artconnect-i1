package org.project.artconnect.persistence;

import org.project.artconnect.dao.impl.ArtworkDao;
import org.project.artconnect.model.Artwork;
import org.project.artconnect.model.Artist;
import org.project.artconnect.config.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation for ArtworkDao.
 */
public class JdbcArtworkDao implements ArtworkDao {

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
    }

    private Artwork extractArtworkInfo(ResultSet rs) throws SQLException {
        Artwork artwork = new Artwork();
        artwork.setTitle(rs.getString("title"));
        artwork.setCreationYear(rs.getObject("creationYear", Integer.class));
        artwork.setType(rs.getString("type"));
        artwork.setMedium(rs.getString("medium"));
        artwork.setDimensions(rs.getString("dimensions"));
        artwork.setDescription(rs.getString("description"));
        artwork.setPrice(rs.getDouble("price"));
        String statusStr = rs.getString("status");
        if (statusStr != null) {
            try {
                artwork.setStatus(Artwork.Status.valueOf(statusStr.toUpperCase()));
            } catch (IllegalArgumentException e) {
                artwork.setStatus(Artwork.Status.FOR_SALE);
            }
        }
        return artwork;
    }

    @Override
    public List<Artwork> findAll() {
        List<Artwork> artworks = new ArrayList<>();
        String sql = "SELECT w.*, a.name AS artistName FROM Artwork w " +
                     "LEFT JOIN Appartient ap ON w.id_artwork = ap.id_artwork " +
                     "LEFT JOIN Artist a ON ap.id_artist = a.id_artist";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Artwork artwork = extractArtworkInfo(rs);
                String artistName = rs.getString("artistName");
                if (artistName != null) {
                    Artist artist = new Artist();
                    artist.setName(artistName);
                    artwork.setArtist(artist);
                }
                artworks.add(artwork);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return artworks;
    }

    @Override
    public void save(Artwork artwork) {
        String insertArtworkSql = "INSERT INTO Artwork (title, creationYear, type, medium, dimensions, description, price, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String selectArtistSql = "SELECT id_artist FROM Artist WHERE name = ?";
        String insertLinkSql = "INSERT INTO Appartient (id_artist, id_artwork) VALUES (?, ?)";

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false); // Transaction

            try (PreparedStatement pstmt = conn.prepareStatement(insertArtworkSql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, artwork.getTitle());
                pstmt.setObject(2, artwork.getCreationYear(), Types.INTEGER);
                pstmt.setString(3, artwork.getType());
                pstmt.setString(4, artwork.getMedium());
                pstmt.setString(5, artwork.getDimensions());
                pstmt.setString(6, artwork.getDescription());
                pstmt.setDouble(7, artwork.getPrice());
                pstmt.setString(8, artwork.getStatus() != null ? artwork.getStatus().name() : Artwork.Status.FOR_SALE.name());
                
                pstmt.executeUpdate();

                int artworkId = -1;
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        artworkId = rs.getInt(1);
                    }
                }

                // Lier l'artiste s'il est dfini
                if (artworkId != -1 && artwork.getArtist() != null && artwork.getArtist().getName() != null) {
                    int artistId = -1;
                    try (PreparedStatement pstmtArtist = conn.prepareStatement(selectArtistSql)) {
                        pstmtArtist.setString(1, artwork.getArtist().getName());
                        try (ResultSet rsArtist = pstmtArtist.executeQuery()) {
                            if (rsArtist.next()) {
                                artistId = rsArtist.getInt(1);
                            }
                        }
                    }
                    if (artistId != -1) {
                        try (PreparedStatement pstmtLink = conn.prepareStatement(insertLinkSql)) {
                            pstmtLink.setInt(1, artistId);
                            pstmtLink.setInt(2, artworkId);
                            pstmtLink.executeUpdate();
                        }
                    }
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Artwork artwork) {
        String sql = "UPDATE Artwork SET creationYear = ?, type = ?, medium = ?, dimensions = ?, description = ?, price = ?, status = ? WHERE title = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, artwork.getCreationYear(), Types.INTEGER);
            pstmt.setString(2, artwork.getType());
            pstmt.setString(3, artwork.getMedium());
            pstmt.setString(4, artwork.getDimensions());
            pstmt.setString(5, artwork.getDescription());
            pstmt.setDouble(6, artwork.getPrice());
            pstmt.setString(7, artwork.getStatus() != null ? artwork.getStatus().name() : Artwork.Status.FOR_SALE.name());
            pstmt.setString(8, artwork.getTitle());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String title) {
        String selectIdSql = "SELECT id_artwork FROM Artwork WHERE title = ?";
        String deleteAppartientSql = "DELETE FROM Appartient WHERE id_artwork = ?";
        String deleteReviewSql = "DELETE FROM Review WHERE id_artwork = ?";
        String deleteReferenceSql = "DELETE FROM Reference WHERE id_artwork = ?";
        String deleteArtworkSql = "DELETE FROM Artwork WHERE id_artwork = ?";

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false); // Transaction pour effacer les cls trangres
            try {
                int artworkId = -1;
                try (PreparedStatement pstmt = conn.prepareStatement(selectIdSql)) {
                    pstmt.setString(1, title);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            artworkId = rs.getInt(1);
                        }
                    }
                }
                if (artworkId != -1) {
                    try (PreparedStatement pstmt = conn.prepareStatement(deleteAppartientSql)) {
                        pstmt.setInt(1, artworkId);
                        pstmt.executeUpdate();
                    }
                    try (PreparedStatement pstmt = conn.prepareStatement(deleteReviewSql)) {
                        pstmt.setInt(1, artworkId);
                        pstmt.executeUpdate();
                    }
                    try (PreparedStatement pstmt = conn.prepareStatement(deleteReferenceSql)) {
                        pstmt.setInt(1, artworkId);
                        pstmt.executeUpdate();
                    }
                    try (PreparedStatement pstmt = conn.prepareStatement(deleteArtworkSql)) {
                        pstmt.setInt(1, artworkId);
                        pstmt.executeUpdate();
                    }
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Artwork> findByArtistName(String artistName) {
        List<Artwork> artworks = new ArrayList<>();
        String sql = "SELECT w.* FROM Artwork w " +
                     "JOIN Appartient ap ON w.id_artwork = ap.id_artwork " +
                     "JOIN Artist a ON ap.id_artist = a.id_artist " +
                     "WHERE a.name = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, artistName);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Artwork artwork = extractArtworkInfo(rs);
                    Artist artist = new Artist();
                    artist.setName(artistName);
                    artwork.setArtist(artist);
                    artworks.add(artwork);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return artworks;
    }
}
