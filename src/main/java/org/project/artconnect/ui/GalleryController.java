package org.project.artconnect.ui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import org.project.artconnect.model.Gallery;
import org.project.artconnect.service.GalleryService;
import org.project.artconnect.util.ServiceProvider;

public class GalleryController {
    @FXML
    private ListView<Gallery> galleryList;

    private final GalleryService galleryService = ServiceProvider.getGalleryService();

    @FXML
    public void initialize() {
        galleryList.setItems(FXCollections.observableArrayList(galleryService.getAllGalleries()));

        // Custom cell factory to show more info
        galleryList.setCellFactory(lv -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Gallery item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName() + " - " + item.getAddress() + " (" + item.getRating() + "/5.0)");
                }
            }
        });
    }
}
