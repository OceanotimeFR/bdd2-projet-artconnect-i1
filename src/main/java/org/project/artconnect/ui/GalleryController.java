package org.project.artconnect.ui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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

        galleryTable.setItems(FXCollections.observableArrayList(galleryService.getAllGalleries()));
    }
}
