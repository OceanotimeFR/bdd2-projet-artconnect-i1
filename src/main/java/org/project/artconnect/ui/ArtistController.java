package org.project.artconnect.ui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import org.project.artconnect.model.Artist;
import org.project.artconnect.model.Discipline;
import org.project.artconnect.service.ArtistService;
import org.project.artconnect.util.ServiceProvider;

public class ArtistController {
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<Discipline> disciplineFilter;
    @FXML
    private TableView<Artist> artistTable;
    @FXML
    private TableColumn<Artist, String> nameColumn;
    @FXML
    private TableColumn<Artist, String> cityColumn;
    @FXML
    private TableColumn<Artist, String> emailColumn;
    @FXML
    private TableColumn<Artist, Integer> yearColumn;

    private final ArtistService artistService = ServiceProvider.getArtistService();

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("contactEmail"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("birthYear"));

        disciplineFilter.setItems(FXCollections.observableArrayList(artistService.getAllDisciplines()));
        refreshTable();
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText();
        Discipline d = disciplineFilter.getValue();
        String dName = (d != null) ? d.getName() : null;
        artistTable.setItems(FXCollections.observableArrayList(artistService.searchArtists(query, dName, null)));
    }

    @FXML
    private void handleReset() {
        searchField.clear();
        disciplineFilter.setValue(null);
        refreshTable();
    }

    private void refreshTable() {
        artistTable.setItems(FXCollections.observableArrayList(artistService.getAllArtists()));
    }

    @FXML
    private void handleAdd() {
        Artist newArtist = new Artist("", "", null, "", "");
        if (showArtistDialog(newArtist, "Add New Artist")) {
            artistService.createArtist(newArtist);
            refreshTable();
        }
    }

    @FXML
    private void handleEdit() {
        Artist selectedArtist = artistTable.getSelectionModel().getSelectedItem();
        if (selectedArtist != null) {
            if (showArtistDialog(selectedArtist, "Edit Artist")) {
                artistService.updateArtist(selectedArtist);
                refreshTable();
            }
        } else {
            showAlert("No Selection", "Please select an artist to edit.");
        }
    }

    @FXML
    private void handleDelete() {
        Artist selectedArtist = artistTable.getSelectionModel().getSelectedItem();
        if (selectedArtist != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete " + selectedArtist.getName() + "?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                artistService.deleteArtist(selectedArtist.getName());
                refreshTable();
            }
        } else {
            showAlert("No Selection", "Please select an artist to delete.");
        }
    }

    private boolean showArtistDialog(Artist artist, String title) {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText("Please enter the artist details:");

        javafx.scene.control.ButtonType saveButtonType = new javafx.scene.control.ButtonType("Save", javafx.scene.control.ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, javafx.scene.control.ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField(artist.getName());
        TextField cityField = new TextField(artist.getCity() != null ? artist.getCity() : "");
        TextField emailField = new TextField(artist.getContactEmail() != null ? artist.getContactEmail() : "");
        TextField yearField = new TextField(artist.getBirthYear() != null ? artist.getBirthYear().toString() : "");

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("City:"), 0, 1);
        grid.add(cityField, 1, 1);
        grid.add(new Label("Email:"), 0, 2);
        grid.add(emailField, 1, 2);
        grid.add(new Label("Birth Year:"), 0, 3);
        grid.add(yearField, 1, 3);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                artist.setName(nameField.getText());
                artist.setCity(cityField.getText());
                artist.setContactEmail(emailField.getText());
                try {
                    artist.setBirthYear(Integer.parseInt(yearField.getText()));
                } catch (NumberFormatException e) {
                    artist.setBirthYear(null);
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
