package org.project.artconnect.ui;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import org.project.artconnect.model.Exhibition;
import org.project.artconnect.model.Review;
import org.project.artconnect.model.Workshop;
import org.project.artconnect.persistence.JdbcReviewDao;
import org.project.artconnect.service.GalleryService;
import org.project.artconnect.service.WorkshopService;
import org.project.artconnect.util.ServiceProvider;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DiscoverController {

    // Modifier ce pattern pour changer le format d'affichage de la date
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd MMMM yyyy").withLocale(Locale.FRENCH);
    @FXML
    private FlowPane discoverPane;

    private final GalleryService galleryService = ServiceProvider.getGalleryService();
    private final WorkshopService workshopService = ServiceProvider.getWorkshopService();

    @FXML
    public void initialize() {
        galleryService.getAllExhibitions().stream().limit(4).forEach(this::addExhibitionCard);
        workshopService.getAllWorkshops().stream().limit(4).forEach(this::addWorkshopCard);
        new JdbcReviewDao().findLastReviews(4).forEach(this::addReviewCard);
    }

    private void addExhibitionCard(Exhibition e) {
        VBox card = new VBox(5);
        card.setPadding(new Insets(10));
        card.setStyle(
                "-fx-background-color: #e3f2fd; -fx-border-color: #2196f3; -fx-border-radius: 5; -fx-background-radius: 5;");
        card.setPrefWidth(250);
        card.getChildren().addAll(
                new Label("FEATURED EXHIBITION"),
                new Label(e.getTitle()) {
                    {
                        setStyle("-fx-font-weight: bold;");
                    }
                },
                new Label("Theme: " + e.getTheme()),
                new Label("Gallery: " + (e.getGallery() != null ? e.getGallery().getName() : "Unknown")));
        discoverPane.getChildren().add(card);
    }

    private void addReviewCard(Review r) {
        String stars = "★".repeat(r.getRating()) + "☆".repeat(5 - r.getRating());
        String artworkTitle = r.getArtwork() != null ? r.getArtwork().getTitle() : "Unknown";
        String reviewer = r.getReviewer() != null ? r.getReviewer().getName() : "Anonymous";
        String date = r.getReviewDate() != null ? r.getReviewDate().format(DATE_FMT) : "";

        VBox card = new VBox(5);
        card.setPadding(new Insets(10));
        card.setStyle(
                "-fx-background-color: #fff8e1; -fx-border-color: #ffc107; -fx-border-radius: 5; -fx-background-radius: 5;");
        card.setPrefWidth(250);
        card.getChildren().addAll(
                new Label("MEMBER REVIEW"),
                new Label(artworkTitle) {{ setStyle("-fx-font-weight: bold;"); }},
                new Label(stars) {{ setStyle("-fx-font-size: 14;"); }},
                new Label(r.getComment() != null ? r.getComment() : ""),
                new Label("— " + reviewer + "  " + date) {{ setStyle("-fx-font-style: italic; -fx-font-size: 11;"); }});
        discoverPane.getChildren().add(card);
    }

    private void addWorkshopCard(Workshop w) {
        VBox card = new VBox(5);
        card.setPadding(new Insets(10));
        card.setStyle(
                "-fx-background-color: #f1f8e9; -fx-border-color: #4caf50; -fx-border-radius: 5; -fx-background-radius: 5;");
        card.setPrefWidth(250);
        card.getChildren().addAll(
                new Label("UPCOMING WORKSHOP"),
                new Label(w.getTitle()) {
                    {
                        setStyle("-fx-font-weight: bold;");
                    }
                },
                new Label("Instructor: " + (w.getInstructor() != null ? w.getInstructor().getName() : "Unknown")),
                new Label("Price: $" + w.getPrice()));
        discoverPane.getChildren().add(card);
    }
}
