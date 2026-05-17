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
import org.project.artconnect.model.Workshop;
import org.project.artconnect.service.WorkshopService;
import org.project.artconnect.util.ServiceProvider;

import java.time.LocalDateTime;

public class WorkshopController {
    @FXML
    private TableView<Workshop> workshopTable;
    @FXML
    private TableColumn<Workshop, String> titleColumn;
    @FXML
    private TableColumn<Workshop, LocalDateTime> dateColumn;
    @FXML
    private TableColumn<Workshop, String> instructorColumn;
    @FXML
    private TableColumn<Workshop, Double> priceColumn;
    @FXML
    private TableColumn<Workshop, String> levelColumn;

    private final WorkshopService workshopService = ServiceProvider.getWorkshopService();

    @FXML
    public void initialize() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
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
        levelColumn.setCellValueFactory(new PropertyValueFactory<>("level"));

        instructorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getInstructor() != null ? cellData.getValue().getInstructor().getName()
                        : "Unknown"));

        refreshTable();
    }

    private void refreshTable() {
        workshopTable.setItems(FXCollections.observableArrayList(workshopService.getAllWorkshops()));
    }

    @FXML
    private void handleAdd() {
        Workshop newWorkshop = new Workshop("", null, null, 0.0);
        if (showWorkshopDialog(newWorkshop, "Add New Workshop")) {
            workshopService.createWorkshop(newWorkshop);
            refreshTable();
        }
    }

    @FXML
    private void handleEdit() {
        Workshop selectedWorkshop = workshopTable.getSelectionModel().getSelectedItem();
        if (selectedWorkshop != null) {
            if (showWorkshopDialog(selectedWorkshop, "Edit Workshop")) {
                workshopService.updateWorkshop(selectedWorkshop);
                refreshTable();
            }
        } else {
            showAlert("No Selection", "Please select a workshop to edit.");
        }
    }

    @FXML
    private void handleDelete() {
        Workshop selectedWorkshop = workshopTable.getSelectionModel().getSelectedItem();
        if (selectedWorkshop != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete " + selectedWorkshop.getTitle() + "?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                // Assuming service has deleteWorkshop by title
                workshopService.deleteWorkshop(selectedWorkshop.getTitle());
                refreshTable();
            }
        } else {
            showAlert("No Selection", "Please select a workshop to delete.");
        }
    }

    private boolean showWorkshopDialog(Workshop workshop, String title) {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText("Please enter the workshop details:");

        javafx.scene.control.ButtonType saveButtonType = new javafx.scene.control.ButtonType("Save", javafx.scene.control.ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, javafx.scene.control.ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField titleField = new TextField(workshop.getTitle());
        TextField levelField = new TextField(workshop.getLevel() != null ? workshop.getLevel() : "");
        TextField priceField = new TextField(String.valueOf(workshop.getPrice()));

        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Level:"), 0, 1);
        grid.add(levelField, 1, 1);
        grid.add(new Label("Price:"), 0, 2);
        grid.add(priceField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                workshop.setTitle(titleField.getText());
                workshop.setLevel(levelField.getText());
                try {
                    workshop.setPrice(Double.parseDouble(priceField.getText()));
                } catch (NumberFormatException e) {
                    workshop.setPrice(0.0);
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
