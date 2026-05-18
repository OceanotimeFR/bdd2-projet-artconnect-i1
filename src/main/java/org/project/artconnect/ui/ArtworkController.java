package org.project.artconnect.ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import java.text.NumberFormat;
import java.util.Locale;
import org.project.artconnect.model.Artwork;
import org.project.artconnect.model.Artist;
import org.project.artconnect.service.ArtworkService;
import org.project.artconnect.util.ServiceProvider;

public class ArtworkController {
    @FXML
    private TableView<Artwork> artworkTable;
    @FXML
    private TableColumn<Artwork, String> titleColumn;
    @FXML
    private TableColumn<Artwork, String> typeColumn;
    @FXML
    private TableColumn<Artwork, Double> priceColumn;
    @FXML
    private TableColumn<Artwork, String> statusColumn;
    @FXML
    private TableColumn<Artwork, String> artistColumn;

    private final ArtworkService artworkService = ServiceProvider.getArtworkService();

    @FXML
    public void initialize() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceColumn.setCellFactory(col -> new TableCell<>() {
            private final NumberFormat fmt = NumberFormat.getNumberInstance(Locale.US);
            { fmt.setMinimumFractionDigits(2); fmt.setMaximumFractionDigits(2); }
            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty);
                setText(empty || value == null ? null : "$" + fmt.format(value));
            }
        });
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        artistColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getArtist() != null ? cellData.getValue().getArtist().getName() : "Unknown"));

        refreshTable();
    }

    private void refreshTable() {
        artworkTable.setItems(FXCollections.observableArrayList(artworkService.getAllArtworks()));
    }

    @FXML
    private void handleAdd() {
        Artwork newArtwork = new Artwork("", null, "", 0.0, null);
        if (showArtworkDialog(newArtwork, "Add New Artwork")) {
            artworkService.createArtwork(newArtwork);
            refreshTable();
        }
    }

    @FXML
    private void handleEdit() {
        Artwork selectedArtwork = artworkTable.getSelectionModel().getSelectedItem();
        if (selectedArtwork != null) {
            if (showArtworkDialog(selectedArtwork, "Edit Artwork")) {
                artworkService.updateArtwork(selectedArtwork);
                refreshTable();
            }
        } else {
            showAlert("No Selection", "Please select an artwork to edit.");
        }
    }

    @FXML
    private void handleDelete() {
        Artwork selectedArtwork = artworkTable.getSelectionModel().getSelectedItem();
        if (selectedArtwork != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete " + selectedArtwork.getTitle() + "?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                // Warning: Missing an explicit deleteArtwork by object/title in the interface. Assuming by title or id.
                // We'll use title as ID based on how we did artist.
                artworkService.deleteArtwork(selectedArtwork.getTitle());
                refreshTable();
            }
        } else {
            showAlert("No Selection", "Please select an artwork to delete.");
        }
    }

    private boolean showArtworkDialog(Artwork artwork, String title) {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText("Please enter the artwork details:");

        javafx.scene.control.ButtonType saveButtonType = new javafx.scene.control.ButtonType("Save", javafx.scene.control.ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, javafx.scene.control.ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField titleField = new TextField(artwork.getTitle());
        TextField yearField = new TextField(artwork.getCreationYear() != null ? String.valueOf(artwork.getCreationYear()) : "");
        TextField typeField = new TextField(artwork.getType() != null ? artwork.getType() : "");
        TextField priceField = new TextField(String.valueOf(artwork.getPrice()));
        TextField artistField = new TextField(artwork.getArtist() != null ? artwork.getArtist().getName() : "");
        TextField exhibitionField = new TextField(artwork.getExhibitionId() != null ? String.valueOf(artwork.getExhibitionId()) : "");

        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Year:"), 0, 1);
        grid.add(yearField, 1, 1);
        grid.add(new Label("Type:"), 0, 2);
        grid.add(typeField, 1, 2);
        grid.add(new Label("Price:"), 0, 3);
        grid.add(priceField, 1, 3);
        grid.add(new Label("Artist name:"), 0, 4);
        grid.add(artistField, 1, 4);
        grid.add(new Label("Exhibition ID:"), 0, 5);
        grid.add(exhibitionField, 1, 5);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                artwork.setTitle(titleField.getText());
                try {
                    artwork.setCreationYear(Integer.parseInt(yearField.getText()));
                } catch (NumberFormatException e) {
                    artwork.setCreationYear(null);
                }
                artwork.setType(typeField.getText());
                try {
                    artwork.setPrice(Double.parseDouble(priceField.getText()));
                } catch (NumberFormatException e) {
                    artwork.setPrice(0.0);
                }
                String artistName = artistField.getText().trim();
                if (!artistName.isEmpty()) {
                    Artist artist = new Artist();
                    artist.setName(artistName);
                    artwork.setArtist(artist);
                }
                try {
                    artwork.setExhibitionId(Integer.parseInt(exhibitionField.getText()));
                } catch (NumberFormatException e) {
                    artwork.setExhibitionId(null);
                }
                return true;
            }
            return false;
        });

        return dialog.showAndWait().orElse(false);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
