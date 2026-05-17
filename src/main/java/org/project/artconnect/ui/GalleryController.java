package org.project.artconnect.ui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
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
import org.project.artconnect.model.Gallery;
import org.project.artconnect.service.GalleryService;
import org.project.artconnect.util.ServiceProvider;

public class GalleryController {
    @FXML
    private TableView<Gallery> galleryTable;
    @FXML
    private TableColumn<Gallery, String> nameColumn;
    @FXML
    private TableColumn<Gallery, Integer> streetNumColumn;
    @FXML
    private TableColumn<Gallery, String> streetNameColumn;
    @FXML
    private TableColumn<Gallery, Integer> zipColumn;
    @FXML
    private TableColumn<Gallery, String> cityColumn;
    @FXML
    private TableColumn<Gallery, String> countryColumn;
    @FXML
    private TableColumn<Gallery, Double> ratingColumn;

    private final GalleryService galleryService = ServiceProvider.getGalleryService();

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        streetNumColumn.setCellValueFactory(new PropertyValueFactory<>("streetNumber"));
        streetNameColumn.setCellValueFactory(new PropertyValueFactory<>("streetName"));
        zipColumn.setCellValueFactory(new PropertyValueFactory<>("zipCode"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));

        refreshTable();
    }

    private void refreshTable() {
        galleryTable.setItems(FXCollections.observableArrayList(galleryService.getAllGalleries()));
    }

    @FXML
    private void handleAdd() {
        Gallery newGallery = new Gallery("", "", "", 0.0);
        if (showGalleryDialog(newGallery, "Add New Gallery")) {
            galleryService.createGallery(newGallery);
            refreshTable();
        }
    }

    @FXML
    private void handleEdit() {
        Gallery selectedGallery = galleryTable.getSelectionModel().getSelectedItem();
        if (selectedGallery != null) {
            if (showGalleryDialog(selectedGallery, "Edit Gallery")) {
                galleryService.updateGallery(selectedGallery);
                refreshTable();
            }
        } else {
            showAlert("No Selection", "Please select a gallery to edit.");
        }
    }

    @FXML
    private void handleDelete() {
        Gallery selectedGallery = galleryTable.getSelectionModel().getSelectedItem();
        if (selectedGallery != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete " + selectedGallery.getName() + "?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                // Assuming service has deleteGallery by name
                galleryService.deleteGallery(selectedGallery.getName());
                refreshTable();
            }
        } else {
            showAlert("No Selection", "Please select a gallery to delete.");
        }
    }

    private boolean showGalleryDialog(Gallery gallery, String title) {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText("Please enter the gallery details:");

        javafx.scene.control.ButtonType saveButtonType = new javafx.scene.control.ButtonType("Save", javafx.scene.control.ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, javafx.scene.control.ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField(gallery.getName());
        TextField streetNameField = new TextField(gallery.getStreetName() != null ? gallery.getStreetName() : "");
        TextField cityField = new TextField(gallery.getCity() != null ? gallery.getCity() : "");
        TextField ratingField = new TextField(String.valueOf(gallery.getRating()));

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Street Name:"), 0, 1);
        grid.add(streetNameField, 1, 1);
        grid.add(new Label("City:"), 0, 2);
        grid.add(cityField, 1, 2);
        grid.add(new Label("Rating:"), 0, 3);
        grid.add(ratingField, 1, 3);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                gallery.setName(nameField.getText());
                gallery.setStreetName(streetNameField.getText());
                gallery.setCity(cityField.getText());
                try {
                    gallery.setRating(Double.parseDouble(ratingField.getText()));
                } catch (NumberFormatException e) {
                    gallery.setRating(0.0);
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
