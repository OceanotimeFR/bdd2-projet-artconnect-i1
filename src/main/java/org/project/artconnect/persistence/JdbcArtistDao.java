package org.project.artconnect.persistence;

import org.project.artconnect.dao.impl.ArtistDao;
import org.project.artconnect.model.Artist;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.project.artconnect.config.DatabaseConfig;
import org.project.artconnect.model.Discipline;

/**
 * JDBC implementation for ArtistDao.
 */
public class JdbcArtistDao implements ArtistDao {

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
    }

    private Artist mapArtist(ResultSet rs) throws SQLException {
        Artist artist = new Artist();
        artist.setName(rs.getString("name"));
        artist.setBio(rs.getString("bio"));
        artist.setBirthYear(rs.getObject("birthYear", Integer.class));
        artist.setContactEmail(rs.getString("contactEmail"));
        artist.setActive(rs.getBoolean("isActive"));
        artist.setPhone(rs.getString("phone"));
        artist.setCity(rs.getString("city"));
        artist.setWebsite(rs.getString("website"));
        artist.setSocialMedia(rs.getString("socialMedia"));

        // Load disciplines for this artist
        try (PreparedStatement pstmt = rs.getStatement().getConnection().prepareStatement(
                "SELECT d.name FROM Discipline d JOIN Exerce e ON d.id_discipline = e.id_discipline WHERE e.id_artist = ?"
        )) {
            pstmt.setInt(1, rs.getInt("id_artist"));
            try (ResultSet drs = pstmt.executeQuery()) {
                List<Discipline> disciplines = new ArrayList<>();
                while (drs.next()) {
                    Discipline d = new Discipline();
                    d.setName(drs.getString("name"));
                    disciplines.add(d);
                }
                artist.setDisciplines(disciplines);
            }
        }
        return artist;
    }

    @Override
    public List<Artist> findAll() {
        List<Artist> artists = new ArrayList<>();
        String sql = "SELECT * FROM Artist";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                artists.add(mapArtist(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return artists;
    }

    @Override
    public void save(Artist artist) {
        String sql = "INSERT INTO Artist (name, bio, birthYear, contactEmail, isActive, phone, city, website, socialMedia) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, artist.getName());
            pstmt.setString(2, artist.getBio());
            pstmt.setObject(3, artist.getBirthYear(), Types.INTEGER);
            pstmt.setString(4, artist.getContactEmail());
            pstmt.setBoolean(5, artist.isActive());
            pstmt.setString(6, artist.getPhone());
            pstmt.setString(7, artist.getCity());
            pstmt.setString(8, artist.getWebsite());
            pstmt.setString(9, artist.getSocialMedia());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Artist artist) {
        String sql = "UPDATE Artist SET bio = ?, birthYear = ?, contactEmail = ?, isActive = ?, phone = ?, city = ?, website = ?, socialMedia = ? WHERE name = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, artist.getBio());
            pstmt.setObject(2, artist.getBirthYear(), Types.INTEGER);
            pstmt.setString(3, artist.getContactEmail());
            pstmt.setBoolean(4, artist.isActive());
            pstmt.setString(5, artist.getPhone());
            pstmt.setString(6, artist.getCity());
            pstmt.setString(7, artist.getWebsite());
            pstmt.setString(8, artist.getSocialMedia());
            pstmt.setString(9, artist.getName());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String artistName) {
        String sql = "DELETE FROM Artist WHERE name = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, artistName);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Artist> findByCity(String city) {
        List<Artist> artists = new ArrayList<>();
        String sql = "SELECT * FROM Artist WHERE city = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, city);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    artists.add(mapArtist(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return artists;
    }

    @Override
    public List<Discipline> findAllDisciplines() {
        List<org.project.artconnect.model.Discipline> disciplines = new ArrayList<>();
        String sql = "SELECT * FROM Discipline";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                org.project.artconnect.model.Discipline d = new org.project.artconnect.model.Discipline();
                d.setName(rs.getString("name"));
                disciplines.add(d);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return disciplines;
    }
}
