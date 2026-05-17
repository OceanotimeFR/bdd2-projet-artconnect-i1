package org.project.artconnect.ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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

        workshopTable.setItems(FXCollections.observableArrayList(workshopService.getAllWorkshops()));
    }
}
