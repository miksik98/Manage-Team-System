package pl.edu.agh.pierogi.controller;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pl.edu.agh.pierogi.config.StageManager;
import pl.edu.agh.pierogi.dao.PersonDAO;
import pl.edu.agh.pierogi.dao.TaskDAO;
import pl.edu.agh.pierogi.dao.TeamDAO;
import pl.edu.agh.pierogi.model.*;
import pl.edu.agh.pierogi.view.View;

import java.util.Optional;

@Controller
public class TeamDetailsController extends BasicController<Team> {

    @Autowired
    @Lazy
    private StageManager stageManager;

    @Autowired
    private PersonDAO personDAO;

    @Autowired
    private TeamDAO teamDAO;

    @Autowired
    private TaskDAO taskDAO;

    @FXML
    private TableView<PersonTeam> peopleTable;

    @FXML
    private TableView<Task> taskTable;

    @FXML
    private TableColumn<Task, String> taskNameColumn;

    @FXML
    private TableColumn<Task, String> executiveFirstnameColumn;

    @FXML
    private TableColumn<Task, String> executiveLastnameColumn;

    @FXML
    private TableColumn<Task, String> isCompletedColumn;

    @FXML
    private TableColumn<PersonTeam, String> firstnameColumn;

    @FXML
    private TableColumn<PersonTeam, String> lastnameColumn;

    @FXML
    private TableColumn<PersonTeam, String> emailColumn;

    @FXML
    private Label teamNameLabel;

    @FXML
    private Label leaderFirstnameLabel;

    @FXML
    private Label leaderLastnameLabel;

    @FXML
    private Label leaderEmailLabel;

    @FXML
    private Label projectTopicLabel;

    @FXML
    private Label projectDescriptionLabel;

    @FXML
    private MenuItem contextMenuShowTaskDescription;

    @FXML
    private MenuItem contextMenuDeleteTask;

    @FXML
    protected void initialize() {
        peopleTable.setItems(FXCollections.observableArrayList());

        taskTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        firstnameColumn.setCellValueFactory(dataValue -> new SimpleStringProperty(dataValue.getValue().getPerson().getFirstName()));
        lastnameColumn.setCellValueFactory(dataValue -> new SimpleStringProperty(dataValue.getValue().getPerson().getLastName()));
        emailColumn.setCellValueFactory(dataValue -> new SimpleStringProperty(dataValue.getValue().getPerson().getEmail()));
        taskNameColumn.setCellValueFactory(dataValue -> new SimpleStringProperty(dataValue.getValue().getName()));
        executiveFirstnameColumn.setCellValueFactory(dataValue -> new SimpleStringProperty(dataValue.getValue().getPersonTeam().getPerson().getFirstName()));
        executiveLastnameColumn.setCellValueFactory(dataValue -> new SimpleStringProperty(dataValue.getValue().getPersonTeam().getPerson().getLastName()));
        isCompletedColumn.setCellValueFactory(dataValue -> new SimpleStringProperty(dataValue.getValue().isCompleted() ? "yes" : "no"));

        contextMenuShowTaskDescription.disableProperty().bind(Bindings.size(taskTable.getSelectionModel().getSelectedItems()).isNotEqualTo(1));
        contextMenuDeleteTask.disableProperty().bind(Bindings.size(taskTable.getSelectionModel().getSelectedItems()).isEqualTo(0));
    }

    @FXML
    protected void handleOkAction(ActionEvent event) {
        stageManager.closeModal();
    }

    @FXML
    private void handleShowTaskDescription(ActionEvent event) {
        stageManager.openModal(View.TASK_DESCRIPTION, taskTable.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void handleDeleteAction(ActionEvent event) {
        if (stageManager.openModal(View.CONFIRMATION, null)) {
            taskDAO.deleteInBatch(taskTable.getSelectionModel().getSelectedItems());
            taskTable.getItems().removeAll(taskTable.getSelectionModel().getSelectedItems());
            taskTable.refresh();
        }
    }

    @Override
    public void setData(Team data) {
        super.setData(data);

        peopleTable.getItems().addAll(data.getPersonTeams());

        for (PersonTeam personTeam : data.getPersonTeams()) {
            for (Task task : personTeam.getTask()) {
                taskTable.getItems().add(task);
            }
        }

        teamNameLabel.setText(data.getName());

        Optional<Person> leader = data.getPersonTeams().stream()
                .map(pt -> pt.getPerson())
                .filter(p -> p.getPersonID() == data.getLeaderID())
                .findFirst();

        leaderFirstnameLabel.setText(leader.isEmpty() ? "" : leader.get().getFirstName());
        leaderLastnameLabel.setText(leader.isEmpty() ? "" : leader.get().getLastName());
        leaderEmailLabel.setText(leader.isEmpty() ? "" : leader.get().getEmail());

        Project project = data.getProject();

        projectTopicLabel.setText(project == null ? "" : project.getTopic());
        projectDescriptionLabel.setText(project == null ? "" : project.getDescription());

    }
}
