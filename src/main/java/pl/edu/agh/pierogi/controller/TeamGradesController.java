package pl.edu.agh.pierogi.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pl.edu.agh.pierogi.config.StageManager;
import pl.edu.agh.pierogi.dao.GradeDAO;
import pl.edu.agh.pierogi.model.Grade;
import pl.edu.agh.pierogi.model.PersonTeam;
import pl.edu.agh.pierogi.model.Team;

import java.util.List;

@Controller
public class TeamGradesController extends BasicController<Team> {

    @FXML
    public TableView<PersonTeam> participantsTable;
    @FXML
    public TableColumn<PersonTeam, String> participantFirstName;
    @FXML
    public TableColumn<PersonTeam, String> participantLastName;
    @FXML
    public Button backButton;
    @Autowired
    @Lazy
    private StageManager stageManager;
    @Autowired
    private GradeDAO gradeDAO;
    @FXML
    private TableView<Grade> gradesTable;
    @FXML
    private TableColumn<Grade, String> descriptionColumn;
    @FXML
    private TableColumn<Grade, Double> valueColumn;
    @FXML
    private TableColumn<Grade, Double> maxValueColumn;

    @FXML
    protected void initialize() {
        participantFirstName.setCellValueFactory(dataValue -> new SimpleStringProperty(dataValue.getValue().getPerson().getFirstName()));
        participantLastName.setCellValueFactory(dataValue -> new SimpleStringProperty(dataValue.getValue().getPerson().getLastName()));

        descriptionColumn.setCellValueFactory(dataValue -> new SimpleStringProperty(dataValue.getValue().getDescription()));
        valueColumn.setCellValueFactory(dataValue -> new SimpleObjectProperty<>(dataValue.getValue().getValue()));
        maxValueColumn.setCellValueFactory(dataValue -> new SimpleObjectProperty<>(dataValue.getValue().getMaxValue()));

        participantsTable.getSelectionModel().selectedItemProperty().addListener((observableValue, personTeam, newPerson) -> {
            gradesTable.setItems(FXCollections.observableList(newPerson.getGrade()));
        });
    }

    @Override
    public void setData(Team data) {
        super.setData(data);

        participantsTable.getItems().addAll(data.getPersonTeams());
    }

    @FXML
    public void handleBackAction(ActionEvent e) {
        stageManager.closeModal();
    }

    public void handleDeleteGradeAction(ActionEvent actionEvent) {
        List<Grade> selectedGrades = gradesTable.getSelectionModel().getSelectedItems();
        gradeDAO.deleteInBatch(selectedGrades);
        gradesTable.getItems().removeAll(selectedGrades);
    }
}
