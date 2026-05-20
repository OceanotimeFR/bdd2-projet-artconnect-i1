package org.project.artconnect.ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import org.project.artconnect.model.Workshop;
import org.project.artconnect.model.Artist;
import org.project.artconnect.service.WorkshopService;
import org.project.artconnect.util.ServiceProvider;

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

    // Pattern pour changer le format d'affichage de la date
    private static final DateTimeFormatter DATE_TIME_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy '-' HH:mm");

    private final WorkshopService workshopService = ServiceProvider.getWorkshopService();

    @FXML
    public void initialize() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime value, boolean empty) {
                super.updateItem(value, empty);
                // Opérateur ternaire --> condition ? valeurSiVrai : valeurSiFaux
                setText(empty || value == null ? null : value.format(DATE_TIME_FMT));
            }
        });
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
        TextField instructorField = new TextField(
                workshop.getInstructor() != null ? workshop.getInstructor().getName() : "");

        DatePicker datePicker = new DatePicker(
                workshop.getDate() != null ? workshop.getDate().toLocalDate() : LocalDate.now());
        TextField timeField = new TextField(
                workshop.getDate() != null ? workshop.getDate().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")) : "");
        timeField.setPromptText("HH:mm");

        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Instructor:"), 0, 1);
        grid.add(instructorField, 1, 1);
        grid.add(new Label("Date:"), 0, 2);
        grid.add(datePicker, 1, 2);
        grid.add(new Label("Time (HH:mm):"), 0, 3);
        grid.add(timeField, 1, 3);
        grid.add(new Label("Level:"), 0, 4);
        grid.add(levelField, 1, 4);
        grid.add(new Label("Price:"), 0, 5);
        grid.add(priceField, 1, 5);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                workshop.setTitle(titleField.getText());
                LocalDate date = datePicker.getValue();
                if (date == null) {
                    date = LocalDate.now();
                }
                LocalTime time = LocalTime.MIDNIGHT;
                String timeText = timeField.getText().trim();
                if (!timeText.isEmpty()) {
                    try {
                        time = LocalTime.parse(timeText, DateTimeFormatter.ofPattern("HH:mm"));
                    } catch (DateTimeParseException e) {
                        // heure invalide : on garde minuit
                        time = LocalTime.MIDNIGHT;
                    }
                }
                workshop.setDate(LocalDateTime.of(date, time));
                workshop.setLevel(levelField.getText());
                try {
                    workshop.setPrice(Double.parseDouble(priceField.getText()));
                } catch (NumberFormatException e) {
                    workshop.setPrice(0.0);
                }
                String instructorName = instructorField.getText().trim();
                if (!instructorName.isEmpty()) {
                    Artist instructor = new Artist();
                    instructor.setName(instructorName);
                    workshop.setInstructor(instructor);
                } else {
                    workshop.setInstructor(null);
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
