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
import org.project.artconnect.model.CommunityMember;
import org.project.artconnect.service.CommunityService;
import org.project.artconnect.util.ServiceProvider;

public class CommunityController {
    @FXML
    private TableView<CommunityMember> memberTable;
    @FXML
    private TableColumn<CommunityMember, String> nameColumn;
    @FXML
    private TableColumn<CommunityMember, String> emailColumn;
    @FXML
    private TableColumn<CommunityMember, String> membershipTypeColumn;

    private final CommunityService communityService = ServiceProvider.getCommunityService();

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        membershipTypeColumn.setCellValueFactory(new PropertyValueFactory<>("membershipType"));

        refreshTable();
    }

    private void refreshTable() {
        memberTable.setItems(FXCollections.observableArrayList(communityService.getAllMembers()));
    }

    @FXML
    private void handleAdd() {
        CommunityMember newMember = new CommunityMember("", "");
        if (showMemberDialog(newMember, "Add New Member")) {
            communityService.createMember(newMember);
            refreshTable();
        }
    }

    @FXML
    private void handleEdit() {
        CommunityMember selectedMember = memberTable.getSelectionModel().getSelectedItem();
        if (selectedMember != null) {
            if (showMemberDialog(selectedMember, "Edit Member")) {
                communityService.updateMember(selectedMember);
                refreshTable();
            }
        } else {
            showAlert("No Selection", "Please select a member to edit.");
        }
    }

    @FXML
    private void handleDelete() {
        CommunityMember selectedMember = memberTable.getSelectionModel().getSelectedItem();
        if (selectedMember != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete " + selectedMember.getName() + "?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                // Assuming service has deleteMember by name/email
                // We'll use email as identifier.
                communityService.deleteMember(selectedMember.getEmail());
                refreshTable();
            }
        } else {
            showAlert("No Selection", "Please select a member to delete.");
        }
    }

    private boolean showMemberDialog(CommunityMember member, String title) {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText("Please enter the member details:");

        javafx.scene.control.ButtonType saveButtonType = new javafx.scene.control.ButtonType("Save", javafx.scene.control.ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, javafx.scene.control.ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField(member.getName());
        TextField emailField = new TextField(member.getEmail() != null ? member.getEmail() : "");

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Email:"), 0, 1);
        grid.add(emailField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                member.setName(nameField.getText());
                member.setEmail(emailField.getText());
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
