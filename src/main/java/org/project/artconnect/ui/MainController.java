package org.project.artconnect.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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

    @FXML
    private void handleAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("À propos");
        alert.setHeaderText("Projet en Base de Données Avancées");
        alert.setContentText(
                """
                        Gestion d'une plateforme artistique.
                        
                        Étudiants :
                          • Fahed TADRIST
                          • Roy TAMWO
                          • Hippolyte VALLAT
                          • Arnaud SEKHARA
                        
                        Enseignant : Anis ZEBIRI
                        
                        
                        Version : 1.0
                        Année : 2025-2026
                        
                        
                        
                        Merci 🖤
                        """
        );
        alert.showAndWait();
    }
}
