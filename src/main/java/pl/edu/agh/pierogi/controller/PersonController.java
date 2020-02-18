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
import pl.edu.agh.pierogi.commands.DeletePersonCommand;
import pl.edu.agh.pierogi.commands.command.Command;
import pl.edu.agh.pierogi.config.StageManager;
import pl.edu.agh.pierogi.dao.PersonDAO;
import pl.edu.agh.pierogi.model.Person;
import pl.edu.agh.pierogi.view.View;

import java.util.List;


@Controller
public class PersonController {

    ObservableList<Person> people;

    @FXML
    private ListView<Command> commandLogView;

    @Autowired
    private PersonDAO personDAO;

    @Autowired
    @Lazy
    private StageManager stageManager;

    @FXML
    private TableView<Person> peopleTable;

    @FXML
    private TableColumn<Person, String> firstNameColumn;

    @FXML
    private TableColumn<Person, String> surnameColumn;

    @FXML
    private TableColumn<Person, String> emailColumn;

    @FXML
    private MenuItem contextMenuShowTeams;

    @FXML
    private Button deleteButton;

    @FXML
    private Button backButton;

    @FXML
    private Button undoButton;

    @FXML
    private Button redoButton;

    @FXML
    protected void initialize() {
        people = FXCollections.observableList(personDAO.findAll());
        peopleTable.setItems(people);
        people.addListener((ListChangeListener<Person>) c -> {
            peopleTable.setItems(people);
        });

        peopleTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        setCommandRegistry();
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        undoButton.disableProperty().bind(Bindings.size(PierogiApplication.peopleCommandRegistry.getCommandStack()).isEqualTo(0));
        redoButton.disableProperty().bind(Bindings.size(PierogiApplication.peopleCommandRegistry.getUndoCommandStack()).isEqualTo(0));

        peopleTable.getSelectionModel().getSelectedIndices().addListener((ListChangeListener<Integer>) change -> {
            if (change.getList().size() < 2) {
                contextMenuShowTeams.setVisible(true);
            } else {
                contextMenuShowTeams.setVisible(false);
            }
        });
    }

    public void setCommandRegistry() {
        commandLogView.setItems(PierogiApplication.peopleCommandRegistry.getCommandStack());
        commandLogView.setCellFactory(lv -> new ListCell<Command>() {
            protected void updateItem(Command item, boolean empty) {
                super.updateItem(item, empty);
                setText((item != null && !empty) ? item.getName() : null);
            }
        });
    }

    @FXML
    public void handleManageTasksAction(ActionEvent event) {
        Person selectedPerson = peopleTable.getSelectionModel().getSelectedItem();
        stageManager.openModal(View.MANAGE_TASKS, selectedPerson);
    }

    @FXML
    public void handleAddAction(ActionEvent event) {
        Person person = new Person();
        if (stageManager.openModal(View.CREATE_PERSON, person)) {
            people.add(person);
        }
    }

    @FXML
    public void handleUndoAction(ActionEvent event) {
        if (!PierogiApplication.peopleCommandRegistry.getCommandStack().isEmpty()) {
            PierogiApplication.peopleCommandRegistry.undo();
            people = FXCollections.observableList(personDAO.findAll());
            peopleTable.setItems(people);
            setCommandRegistry();
        }
    }

    @FXML
    public void handleRedoAction(ActionEvent event) {
        if (!PierogiApplication.peopleCommandRegistry.getUndoCommandStack().isEmpty()) {
            PierogiApplication.peopleCommandRegistry.redo();
            people = FXCollections.observableList(personDAO.findAll());
            peopleTable.setItems(people);
            setCommandRegistry();
        }
    }

    @FXML
    public void handleDeleteAction(ActionEvent event) {
        List<Person> peopleToDelete = peopleTable.getSelectionModel().getSelectedItems();

        if (peopleToDelete != null) {
            DeletePersonCommand deletePersonCommand = new DeletePersonCommand(personDAO, peopleToDelete);
            PierogiApplication.peopleCommandRegistry.executeCommand(deletePersonCommand);
            people.removeAll(peopleToDelete);

            peopleTable.getSelectionModel().clearSelection();
        }
    }

    @FXML
    private void handleBackAction(ActionEvent event) {
        stageManager.switchScene(View.MAIN);
    }

    public void handleShowTeamsAction(ActionEvent event) {
        Person selectedPerson = peopleTable.getSelectionModel().getSelectedItem();

        stageManager.openModal(View.PERSON_TEAMS, selectedPerson);
    }
}
