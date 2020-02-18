package pl.edu.agh.pierogi.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pl.edu.agh.pierogi.config.StageManager;
import pl.edu.agh.pierogi.model.Project;
import pl.edu.agh.pierogi.model.Team;

@Controller
public class ProjectTeamController extends BasicController<Project> {
    @Autowired
    @Lazy
    private StageManager stageManager;

    @FXML
    private TableView<Team> teamsTable;

    @FXML
    private TableColumn<Team, String> nameColumn;


    @FXML
    private TableColumn<Team, Integer> countMembersColumn;

    @FXML
    protected void initialize() {
        teamsTable.setItems(FXCollections.observableArrayList());

        nameColumn.setCellValueFactory(dataValue -> new SimpleStringProperty(dataValue.getValue().getName()));
        countMembersColumn.setCellValueFactory(dataValue -> new SimpleObjectProperty<>(dataValue.getValue().getPersonTeams().size()));
    }

    @FXML
    protected void handleOkAction(ActionEvent event) {
        stageManager.closeModal();
    }

    @Override
    public void setData(Project data) {
        super.setData(data);

        teamsTable.getItems().addAll(data.getTeams());
    }
}
