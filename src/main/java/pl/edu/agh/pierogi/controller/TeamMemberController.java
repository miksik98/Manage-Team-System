package pl.edu.agh.pierogi.controller;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pl.edu.agh.pierogi.config.StageManager;
import pl.edu.agh.pierogi.dao.PersonDAO;
import pl.edu.agh.pierogi.dao.PersonTeamDAO;
import pl.edu.agh.pierogi.model.Person;
import pl.edu.agh.pierogi.model.Team;

import java.util.List;

@Controller
public class TeamMemberController extends BasicController<Team> {

    @Autowired
    private PersonDAO personDAO;

    @Autowired
    private PersonTeamDAO personTeamDAO;

    @Autowired
    @Lazy
    private StageManager stageManager;

    private ObservableList<Person> people;

    private ObservableList<Person> members;

    @FXML
    private TableView<Person> membersTable;

    @FXML
    private TableView<Person> peopleTable;

    @FXML
    private TableColumn<Person, String> firstnameColumn;

    @FXML
    private TableColumn<Person, String> lastnameColumn;

    @FXML
    private TableColumn<Person, String> memFirstnameColumn;

    @FXML
    private TableColumn<Person, String> memLastnameColumn;

    @FXML
    private Button addButton;

    @FXML
    private Button deleteButton;

    @FXML
    protected void initialize() {
        firstnameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastnameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        memFirstnameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        memLastnameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        peopleTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        membersTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        peopleTable.getSelectionModel().getSelectedIndices().addListener((ListChangeListener<Integer>) change -> {
            if (change.getList().size() == 0) {
                addButton.setDisable(true);
            } else {
                addButton.setDisable(false);
            }
        });

        membersTable.getSelectionModel().getSelectedIndices().addListener((ListChangeListener<Integer>) change -> {
            if (change.getList().size() == 0) {
                deleteButton.setDisable(true);
            } else {
                deleteButton.setDisable(false);
            }
        });
    }

    @Override
    public void setData(Team data) {
        super.setData(data);

        people = FXCollections.observableList(personDAO.getPeopleNotFromTeam(data));

        peopleTable.setItems(people);
        people.addListener((ListChangeListener<Person>) c -> {
            peopleTable.setItems(people);
        });

        members = FXCollections.observableList(personDAO.getPeopleFromTeam(data));

        membersTable.setItems(members);
        members.addListener((ListChangeListener<Person>) c -> {
            membersTable.setItems(members);
        });

        addButton.setDisable(true);
        deleteButton.setDisable(true);

    }

    @FXML
    protected void handleAddAction(ActionEvent event) {
        List<Person> selectedPeople = peopleTable.getSelectionModel().getSelectedItems();
        selectedPeople.forEach(person -> personTeamDAO.savePersonTeam(data, person));
        members.addAll(selectedPeople);
        people.removeAll(selectedPeople);
        peopleTable.getSelectionModel().clearSelection();
    }

    @FXML
    protected void handleDeleteAction(ActionEvent event) {
        List<Person> selectedPeople = membersTable.getSelectionModel().getSelectedItems();
        selectedPeople.forEach(person -> personTeamDAO.deleteByPerson(person));
        people.addAll(selectedPeople);
        members.removeAll(selectedPeople);
        membersTable.getSelectionModel().clearSelection();
    }

    @FXML
    protected void handleOkAction(ActionEvent event) {
        stageManager.closeModal();
    }
}
