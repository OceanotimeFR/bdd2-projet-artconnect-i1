package org.project.artconnect.persistence;

import org.project.artconnect.config.DatabaseConfig;
import org.project.artconnect.model.Artwork;
import org.project.artconnect.model.CommunityMember;
import org.project.artconnect.model.Review;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcReviewDao {

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
    }

    /**
     * Retourne les N derniers avis triés par date décroissante.
     */
    public List<Review> findLastReviews(int limit) {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT r.rating, r.comment, r.reviewDate, a.title AS artworkTitle, m.name  AS memberName " +
                     "FROM Review r " +
                     "JOIN Artwork a ON r.id_artwork = a.id_artwork " +
                     "JOIN CommunityMember m ON r.id_member = m.id_member " +
                     "ORDER BY r.reviewDate DESC " +
                     "LIMIT ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, limit);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Review review = new Review();
                    review.setRating(Integer.parseInt(rs.getString("rating")));
                    review.setComment(rs.getString("comment"));
                    Date d = rs.getDate("reviewDate");
                    if (d != null) review.setReviewDate(d.toLocalDate());

                    Artwork artwork = new Artwork();
                    artwork.setTitle(rs.getString("artworkTitle"));
                    review.setArtwork(artwork);

                    CommunityMember member = new CommunityMember();
                    member.setName(rs.getString("memberName"));
                    review.setReviewer(member);

                    reviews.add(review);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviews;
    }
}
