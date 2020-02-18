package pl.edu.agh.pierogi.controller;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pl.edu.agh.pierogi.config.StageManager;
import pl.edu.agh.pierogi.dao.TaskDAO;
import pl.edu.agh.pierogi.model.Person;
import pl.edu.agh.pierogi.model.PersonTeam;
import pl.edu.agh.pierogi.model.Task;
import pl.edu.agh.pierogi.view.View;

@Controller
public class ManageTasksController extends BasicController<Person> {
    @FXML
    public MenuItem contextMenuShowDescription;
    @Lazy
    @Autowired
    private StageManager stageManager;
    @Autowired
    private TaskDAO taskDAO;

    @FXML
    private Button doneButton;

    @FXML
    private Button notDoneButton;

    @FXML
    private Button deleteButton;

    @FXML
    private TableView<Task> taskTable;

    @FXML
    private TableColumn<Task, String> taskNameColumn;

    @FXML
    private TableColumn<Task, String> projectNameColumn;

    @FXML
    private TableColumn<Task, String> teamNameColumn;

    @FXML
    private TableColumn<Task, String> isCompletedColumn;

    @FXML
    private Label personNameLabel;

    @FXML
    private void handleOkAction(ActionEvent event) {
        stageManager.closeModal();
    }

    @FXML
    protected void initialize() {
        taskNameColumn.setCellValueFactory(dataValue -> new SimpleStringProperty(dataValue.getValue().getName()));
        teamNameColumn.setCellValueFactory(dataValue -> new SimpleStringProperty(dataValue.getValue().getPersonTeam().getTeam().getName()));
        projectNameColumn.setCellValueFactory(dataValue -> new SimpleStringProperty(dataValue.getValue().getPersonTeam().getTeam().getProject().getTopic()));
        isCompletedColumn.setCellValueFactory(dataValue -> new SimpleStringProperty(dataValue.getValue().isCompleted() ? "yes" : "no"));

        taskTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        doneButton.disableProperty().bind(Bindings.size(taskTable.getSelectionModel().getSelectedItems()).isNotEqualTo(1).or(Bindings.createBooleanBinding(() -> taskTable.getSelectionModel().getSelectedItems().size() > 0 && taskTable.getSelectionModel().getSelectedItems().get(0).isCompleted(), taskTable.getSelectionModel().getSelectedItems())));
        notDoneButton.disableProperty().bind(Bindings.size(taskTable.getSelectionModel().getSelectedItems()).isNotEqualTo(1).or(Bindings.createBooleanBinding(() -> taskTable.getSelectionModel().getSelectedItems().size() > 0 && !taskTable.getSelectionModel().getSelectedItems().get(0).isCompleted(), taskTable.getSelectionModel().getSelectedItems())));
        deleteButton.disableProperty().bind(Bindings.size(taskTable.getSelectionModel().getSelectedItems()).isEqualTo(0));
        contextMenuShowDescription.disableProperty().bind(Bindings.size(taskTable.getSelectionModel().getSelectedItems()).isNotEqualTo(1));
    }

    @FXML
    private void handleDoneAction(ActionEvent event) {
        Task task = taskTable.getSelectionModel().getSelectedItem();
        task.setCompleted(true);
        taskDAO.update(task);
        updateModel();
    }

    @FXML
    private void handleShowDescriptionAction(ActionEvent event) {
        stageManager.openModal(View.TASK_DESCRIPTION, taskTable.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void handleNotDoneAction(ActionEvent event) {
        Task task = taskTable.getSelectionModel().getSelectedItem();
        task.setCompleted(false);
        taskDAO.update(task);
        updateModel();
    }

    @FXML
    private void handleDeleteAction(ActionEvent event) {
        if (stageManager.openModal(View.CONFIRMATION, null)) {
            taskDAO.deleteInBatch(taskTable.getSelectionModel().getSelectedItems());
            taskTable.getItems().removeAll(taskTable.getSelectionModel().getSelectedItems());
            updateModel();
        }
    }

    private void updateModel() {
        taskTable.refresh();
        taskTable.getSelectionModel().clearSelection();
    }

    @Override
    public void setData(Person data) {
        super.setData(data);
        personNameLabel.setText(data.toString());

        for (PersonTeam personTeam : data.getPersonTeams()) {
            for (Task task : personTeam.getTask()) {
                taskTable.getItems().add(task);
            }
        }
    }
}
