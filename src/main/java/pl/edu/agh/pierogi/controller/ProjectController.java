package pl.edu.agh.pierogi.controller;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pl.edu.agh.pierogi.PierogiApplication;
import pl.edu.agh.pierogi.commands.DeleteProjectCommand;
import pl.edu.agh.pierogi.commands.command.Command;
import pl.edu.agh.pierogi.config.StageManager;
import pl.edu.agh.pierogi.dao.ProjectDAO;
import pl.edu.agh.pierogi.model.Project;
import pl.edu.agh.pierogi.view.View;

import java.util.List;

@Controller
public class ProjectController {
    ObservableList<Project> projects;

    @FXML
    private ListView<Command> commandLogView;

    @Autowired
    private ProjectDAO projectDAO;

    @Lazy
    @Autowired
    private StageManager stageManager;

    @FXML
    private Button addButton;

    @FXML
    private Button backButton;

    @FXML
    private Button undoButton;

    @FXML
    private Button redoButton;

    @FXML
    private MenuItem contextMenuShowTeams;

    @FXML
    private TableView<Project> projectsTable;

    @FXML
    private TableColumn<Project, String> topicColumn;

    @FXML
    private TableColumn<Project, String> descriptionColumn;


    @FXML
    protected void initialize() {
        projects = FXCollections.observableArrayList(projectDAO.findAll());
        projectsTable.setItems(projects);

        projects.addListener((ListChangeListener<Project>) c -> {
            projectsTable.setItems(projects);
        });

        projectsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        setCommandRegistry();
        topicColumn.setCellValueFactory(new PropertyValueFactory<>("topic"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        undoButton.disableProperty().bind(Bindings.size(PierogiApplication.projectsCommandRegistry.getCommandStack()).isEqualTo(0));
        redoButton.disableProperty().bind(Bindings.size(PierogiApplication.projectsCommandRegistry.getUndoCommandStack()).isEqualTo(0));

        projectsTable.getSelectionModel().getSelectedIndices().addListener((ListChangeListener<Integer>) change -> {
            if (change.getList().size() < 2) {
                contextMenuShowTeams.setVisible(true);
            } else {
                contextMenuShowTeams.setVisible(false);
            }
        });

    }

    public void setCommandRegistry() {
        commandLogView.setItems(PierogiApplication.projectsCommandRegistry.getCommandStack());
        commandLogView.setCellFactory(lv -> new ListCell<Command>() {
            protected void updateItem(Command item, boolean empty) {
                super.updateItem(item, empty);
                setText((item != null && !empty) ? item.getName() : null);
            }
        });
    }

    @FXML
    private void handleAddAction(ActionEvent event) {
        Project project = new Project();
        if (stageManager.openModal(View.CREATE_PROJECT, project)) {
            if (project.getTopic() != null)
                projects.add(project);
        }
    }

    @FXML
    public void handleUndoAction(ActionEvent event) {
        if (!PierogiApplication.projectsCommandRegistry.getCommandStack().isEmpty()) {
            PierogiApplication.projectsCommandRegistry.undo();
            projects = FXCollections.observableList(projectDAO.findAll());
            projectsTable.setItems(projects);
            setCommandRegistry();
        }
    }

    @FXML
    public void handleRedoAction(ActionEvent event) {
        if (!PierogiApplication.projectsCommandRegistry.getUndoCommandStack().isEmpty()) {
            PierogiApplication.projectsCommandRegistry.redo();
            projects = FXCollections.observableList(projectDAO.findAll());
            projectsTable.setItems(projects);
            setCommandRegistry();
        }
    }

    @FXML
    private void handleRemoveAction() {
        List<Project> selectedProjects = projectsTable.getSelectionModel().getSelectedItems();

        if (!selectedProjects.isEmpty()) {
            DeleteProjectCommand deleteProjectCommand = new DeleteProjectCommand(projectDAO, selectedProjects);
            PierogiApplication.projectsCommandRegistry.executeCommand(deleteProjectCommand);
            projects.removeAll(selectedProjects);
        }

    }

    @FXML
    private void handleBackAction(ActionEvent event) {
        stageManager.switchScene(View.MAIN);
    }

    @FXML
    private void handleShowTeamsAction(ActionEvent event) {
        Project selectedProject = projectsTable.getSelectionModel().getSelectedItem();

        stageManager.openModal(View.PROJECT_TEAMS, selectedProject);
    }
}
