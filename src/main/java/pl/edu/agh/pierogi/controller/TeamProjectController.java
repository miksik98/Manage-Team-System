package pl.edu.agh.pierogi.controller;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pl.edu.agh.pierogi.config.StageManager;
import pl.edu.agh.pierogi.dao.ProjectDAO;
import pl.edu.agh.pierogi.dao.TeamDAO;
import pl.edu.agh.pierogi.model.Project;
import pl.edu.agh.pierogi.model.Team;

@Controller
public class TeamProjectController extends BasicController<Team> {

    @FXML
    public Button setButton;
    @Autowired
    @Lazy
    private StageManager stageManager;
    @Autowired
    private TeamDAO teamDAO;
    @Autowired
    private ProjectDAO projectDAO;
    @FXML
    private TableView<Project> projectsTable;
    @FXML
    private TableColumn<Project, String> projectTopic;
    @FXML
    private TableColumn<Project, Integer> projectTeamsAmount;

    @FXML
    protected void initialize() {
        ObservableList<Project> projects = FXCollections.observableArrayList(projectDAO.findAll());
        projectsTable.setItems(projects);

        projectTopic.setCellValueFactory(dataValue -> new SimpleStringProperty(dataValue.getValue().getTopic()));
        projectTeamsAmount.setCellValueFactory(dataValue -> new SimpleObjectProperty<>(dataValue.getValue().getTeams().size()));

        setButton.disableProperty().bind(Bindings.size(projectsTable.getSelectionModel().getSelectedItems()).isNotEqualTo(1));
    }

    public void handleSetAction(ActionEvent event) {
        Project selectedProject = projectsTable.getSelectionModel().getSelectedItem();
        data.setProject(selectedProject);

        projectDAO.update(selectedProject);

        stageManager.closeModal();
    }
}
