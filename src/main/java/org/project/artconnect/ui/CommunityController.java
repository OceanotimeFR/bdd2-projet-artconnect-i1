package org.project.artconnect.ui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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

        memberTable.setItems(FXCollections.observableArrayList(communityService.getAllMembers()));
    }
}
