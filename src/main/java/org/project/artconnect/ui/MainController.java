package org.project.artconnect.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;

public class MainController {
    @FXML
    private TabPane mainTabPane;

    @FXML
    public void initialize() {
        // Initialization logic if needed
    }

    @FXML
    private void handleExit() {
        Platform.exit();
    }
}
