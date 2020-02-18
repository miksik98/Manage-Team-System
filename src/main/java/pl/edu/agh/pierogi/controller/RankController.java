package pl.edu.agh.pierogi.controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pl.edu.agh.pierogi.config.StageManager;
import pl.edu.agh.pierogi.dao.PersonDAO;
import pl.edu.agh.pierogi.dao.TeamDAO;
import pl.edu.agh.pierogi.model.Person;
import pl.edu.agh.pierogi.model.Team;
import pl.edu.agh.pierogi.rank.*;
import pl.edu.agh.pierogi.view.View;

import java.util.Arrays;

@Controller
public class RankController {
    public TextField greaterThanTextField;
    public TextField lesserThanTextField;
    public TableView<Person> peopleTable;
    public TableColumn<Person, String> firstNameColumn;
    public TableColumn<Person, String> surnameColumn;
    public TableColumn<Person, Double> pointsColumn;
    public ChoiceBox<Team> teamSelect;
    public ChoiceBox<String> strategySelect;
    ObjectProperty<ValueComputingStrategy> valueStrategy = new SimpleObjectProperty<>();
    @Autowired
    @Lazy
    private StageManager stageManager;
    @Autowired
    private PersonDAO personDAO;
    @Autowired
    private TeamDAO teamDAO;
    private ObservableList<Person> people;

    @FXML
    private void initialize() {
        strategySelect.setItems(FXCollections.observableList(Arrays.asList(Strategy.POINTS.getName(), Strategy.PERCENT.getName())));
        strategySelect.getSelectionModel().select(Strategy.POINTS.getName());

        strategySelect.getSelectionModel().selectedItemProperty().addListener((observableValue, s, newValue) -> {
            if (newValue.equals(Strategy.POINTS.getName())) {
                valueStrategy.set(new PointsStrategy());
            } else {
                valueStrategy.set(new PercentStrategy());
            }
        });

        valueStrategy.addListener((observableValue, valueComputingStrategy, newStrategy) -> {
            peopleTable.getColumns().remove(pointsColumn);
            pointsColumn.setCellValueFactory(dataValue -> newStrategy.getValue(dataValue.getValue(), teamSelect.getSelectionModel().getSelectedItem()));
            peopleTable.getColumns().add(pointsColumn);
        });

        valueStrategy.set(new PointsStrategy());

        people = FXCollections.observableList(personDAO.findAll());
        peopleTable.setItems(people);

        firstNameColumn.setCellValueFactory(dataValue -> new SimpleStringProperty(dataValue.getValue().getFirstName()));
        surnameColumn.setCellValueFactory(dataValue -> new SimpleStringProperty(dataValue.getValue().getLastName()));
//        pointsColumn.setCellValueFactory(dataValue -> valueStrategy.get().getValue(dataValue.getValue(), teamSelect.getSelectionModel().getSelectedItem()));

        pointsColumn.setSortType(TableColumn.SortType.DESCENDING);
        peopleTable.getSortOrder().add(pointsColumn);

//        pointsColumn.setSortable(false);

        ObservableList<Team> teamList = FXCollections.observableList(teamDAO.findAll());
        teamSelect.setItems(teamList);


    }

    public void handleBackAction(ActionEvent actionEvent) {
        stageManager.switchScene(View.MAIN);
    }

    public void handleSetFilterAction(ActionEvent actionEvent) {
        Team team = teamSelect.getSelectionModel().getSelectedItem();

//        pointsColumn.setCellValueFactory(dataValue -> (new PointsStrategy()).getValue(dataValue.getValue(), team));

        Double greaterThanValue = (greaterThanTextField.getText() != null && !greaterThanTextField.getText().isEmpty()) ? Double.parseDouble(greaterThanTextField.getText()) : null;
        Double lesserThanValue = (lesserThanTextField.getText() != null && !lesserThanTextField.getText().isEmpty()) ? Double.parseDouble(lesserThanTextField.getText()) : null;

        Criteria teamCriteria = new TeamCriteria(team);
        Criteria graterThanCriteria = new GraterThanCriteria(greaterThanValue, team);
        Criteria lesserThanCriteria = new LesserThanCriteria(lesserThanValue, team);
        Criteria greaterOrEqualCriteria = new OrCriteria(graterThanCriteria, new EqualsToCriteria(greaterThanValue, team));
        Criteria lesserOrEqualCriteria = new OrCriteria(lesserThanCriteria, new EqualsToCriteria(lesserThanValue, team));
        Criteria pointsCriteria = new AndCriteria(lesserOrEqualCriteria, greaterOrEqualCriteria);
        Criteria query = new AndCriteria(teamCriteria, pointsCriteria);
        people.setAll(query.meetCriteria(personDAO.findAll()));

        sortPointsColumn();
    }

    public void handleClearFilterAction(ActionEvent actionEvent) {
        greaterThanTextField.setText("");
        lesserThanTextField.setText("");

        teamSelect.getSelectionModel().clearSelection();
        people.setAll(personDAO.findAll());

        sortPointsColumn();
    }

    private void sortPointsColumn() {
        pointsColumn.setSortable(true);
        peopleTable.sort();
        pointsColumn.setSortable(false);
    }

    private enum Strategy {
        POINTS("POINTS"), PERCENT("PERCENT");

        private String name;

        Strategy(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
}
