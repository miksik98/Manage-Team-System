package pl.edu.agh.pierogi.controller;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleSetProperty;
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
import pl.edu.agh.pierogi.commands.DeleteTeamCommand;
import pl.edu.agh.pierogi.commands.command.Command;
import pl.edu.agh.pierogi.config.StageManager;
import pl.edu.agh.pierogi.dao.TeamDAO;
import pl.edu.agh.pierogi.model.PersonTeam;
import pl.edu.agh.pierogi.model.Team;
import pl.edu.agh.pierogi.view.View;

import java.util.List;

@Controller
public class TeamController extends BasicController<Team> {

    @FXML
    public MenuItem contextMenuShowDetails;
    @FXML
    public MenuItem contextMenuSetProject;
    @FXML
    public MenuItem contextMenuSetLeader;
    @FXML
    public MenuItem contextMenuAddMember;
    @FXML
    public MenuItem contextMenuGrade;
    @FXML
    public MenuItem contextMenuNewTask;
    ObservableList<Team> teams;
    @FXML
    private ListView<Command> commandLogView;
    @Autowired
    private TeamDAO teamDAO;
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
    private TableView<Team> teamsTable;

    @FXML
    private TableColumn<Team, String> nameColumn;

    @FXML
    private TableColumn<Team, Integer> countMembersColumn;

    @FXML
    protected void initialize() {
        teams = FXCollections.observableArrayList(teamDAO.findAll());
        teamsTable.setItems(teams);

        teams.addListener((ListChangeListener<Team>) c -> {
            teamsTable.setItems(teams);
        });

        teamsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        setCommandRegistry();
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        countMembersColumn.setCellValueFactory(dataValue -> new SimpleObjectProperty<>(dataValue.getValue().getPersonTeams().size()));

        undoButton.disableProperty().bind(Bindings.size(PierogiApplication.teamsCommandRegistry.getCommandStack()).isEqualTo(0));
        redoButton.disableProperty().bind(Bindings.size(PierogiApplication.teamsCommandRegistry.getUndoCommandStack()).isEqualTo(0));

        teamsTable.getSelectionModel().getSelectedIndices().addListener((ListChangeListener<Integer>) change -> {
            if (change.getList().size() < 2) {
                contextMenuShowDetails.setVisible(true);
                contextMenuAddMember.setVisible(true);
                contextMenuSetLeader.setVisible(true);
                contextMenuSetProject.setVisible(true);
                contextMenuGrade.setVisible(true);
            } else {
                contextMenuShowDetails.setVisible(false);
                contextMenuAddMember.setVisible(false);
                contextMenuSetLeader.setVisible(false);
                contextMenuSetProject.setVisible(false);
                contextMenuGrade.setVisible(false);
            }
        });

        contextMenuNewTask.disableProperty().bind(Bindings.size(teamsTable.getSelectionModel().getSelectedItems()).isNotEqualTo(1).or(Bindings.createBooleanBinding(() -> teamsTable.getSelectionModel().getSelectedItems().size() > 0 && teamsTable.getSelectionModel().getSelectedItems().get(0).getProject() == null, teamsTable.getSelectionModel().getSelectedItems())));

        teamsTable.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    contextMenuSetProject.disableProperty().bind(Bindings.isNotNull(new SimpleObjectProperty<>(newValue.getProject())));
                    contextMenuSetLeader.disableProperty().bind(Bindings.isEmpty(new SimpleSetProperty<PersonTeam>(FXCollections.observableSet(newValue.getPersonTeams()))));
                });
    }

    public void setCommandRegistry() {
        commandLogView.setItems(PierogiApplication.teamsCommandRegistry.getCommandStack());
        commandLogView.setCellFactory(lv -> new ListCell<Command>() {
            protected void updateItem(Command item, boolean empty) {
                super.updateItem(item, empty);
                setText((item != null && !empty) ? item.getName() : null);
            }
        });
    }

    @FXML
    private void handleAddAction(ActionEvent event) {
        Team team = new Team();
        if (stageManager.openModal(View.CREATE_TEAM, team)) {
            if (team.getName() != null && !team.getName().isEmpty())
                teams.add(team);
        }
    }

    @FXML
    private void handleShowGrades(ActionEvent e) {
        Team selectedTeam = teamsTable.getSelectionModel().getSelectedItem();
        stageManager.openModal(View.TEAM_GRADES, selectedTeam);
    }

    @FXML
    private void handleNewTask(ActionEvent e) {
        Team selectedTeam = teamsTable.getSelectionModel().getSelectedItem();
        stageManager.openModal(View.NEW_TASK, selectedTeam);
    }

    @FXML
    public void handleUndoAction(ActionEvent event) {
        if (!PierogiApplication.teamsCommandRegistry.getCommandStack().isEmpty()) {
            PierogiApplication.teamsCommandRegistry.undo();
            teams = FXCollections.observableList(teamDAO.findAll());
            teamsTable.setItems(teams);
            setCommandRegistry();
        }
    }

    @FXML
    public void handleRedoAction(ActionEvent event) {
        if (!PierogiApplication.teamsCommandRegistry.getUndoCommandStack().isEmpty()) {
            PierogiApplication.teamsCommandRegistry.redo();
            teams = FXCollections.observableList(teamDAO.findAll());
            teamsTable.setItems(teams);
            setCommandRegistry();
        }
    }

    @FXML
    private void handleRemoveAction() {
        List<Team> selectedTeams = teamsTable.getSelectionModel().getSelectedItems();

        if (!selectedTeams.isEmpty()) {
            DeleteTeamCommand deleteTeamCommand = new DeleteTeamCommand(teamDAO, selectedTeams);
            PierogiApplication.teamsCommandRegistry.executeCommand(deleteTeamCommand);
            teams.removeAll(selectedTeams);
        }

    }

    @FXML
    private void handleBackAction(ActionEvent event) {
        stageManager.switchScene(View.MAIN);
    }

    @FXML
    protected void handleShowDetailsAction(ActionEvent actionEvent) {
        Team selectedTeam = teamsTable.getSelectionModel().getSelectedItem();
        stageManager.openModal(View.TEAM_DETAILS, selectedTeam);
    }

    @FXML
    protected void handleSetProjectAction(ActionEvent event) {
        Team selectedTeam = teamsTable.getSelectionModel().getSelectedItem();
        stageManager.openModal(View.SET_PROJECT, selectedTeam);
    }

    @FXML
    protected void handleSetLeaderAction(ActionEvent event) {
        Team selectedTeam = teamsTable.getSelectionModel().getSelectedItem();
        stageManager.openModal(View.SET_LEADER, selectedTeam);
    }

    @FXML
    protected void handleAddMemberAction(ActionEvent event) {
        Team selectedTeam = teamsTable.getSelectionModel().getSelectedItem();
        stageManager.openModal(View.ADD_MEMBERS, selectedTeam);
        teamsTable.refresh();
    }

    @FXML
    protected void handleGrade(ActionEvent event) {
        Team selectedTeam = teamsTable.getSelectionModel().getSelectedItem();
        stageManager.openModal(View.GRADE, selectedTeam);
    }
}
