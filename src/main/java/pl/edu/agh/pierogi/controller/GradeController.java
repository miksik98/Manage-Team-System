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
import pl.edu.agh.pierogi.dao.GradeDAO;
import pl.edu.agh.pierogi.dao.PersonTeamDAO;
import pl.edu.agh.pierogi.model.Grade;
import pl.edu.agh.pierogi.model.PersonTeam;
import pl.edu.agh.pierogi.model.Team;
import pl.edu.agh.pierogi.view.View;

import java.util.List;
import java.util.regex.Pattern;

@Controller
public class GradeController extends BasicController<Team> {
    @Autowired
    public PersonTeamDAO personTeamDAO;
    @FXML
    public TextArea descriptionArea;
    @FXML
    public TextField maxValueField;
    @FXML
    public TextField valueField;
    @FXML
    public TableView<PersonTeam> participantsTable;
    @FXML
    public TableColumn<PersonTeam, String> participantFirstName;
    @FXML
    public TableColumn<PersonTeam, String> participantLastName;
    @FXML
    public MenuItem contextMenuShowTasks;
    @Lazy
    @Autowired
    private StageManager stageManager;
    @Autowired
    private GradeDAO gradeDAO;
    @FXML
    private Button gradeButton;

    @FXML
    protected void handleGradeAction(ActionEvent e) {
        if (isInputValid()) {
            List<PersonTeam> selectedItems = participantsTable.getSelectionModel().getSelectedItems();
            selectedItems.forEach(person -> {
                Grade grade = new Grade(Double.parseDouble(valueField.textProperty().getValue()), Double.parseDouble(maxValueField.textProperty().getValue()), descriptionArea.textProperty().getValue(), person);
                gradeDAO.save(grade);
            });
            participantsTable.getItems().removeAll(selectedItems);
        }
    }

    private boolean isInputValid() {
        Pattern validEditingState = Pattern.compile("(([1-9][0-9]*)|0)?(\\.[0-9]*)?");
        return validEditingState.matcher(maxValueField.textProperty().getValue()).matches();
    }

    @FXML
    protected void initialize() {
        participantFirstName.setCellValueFactory(dataValue -> new SimpleStringProperty(dataValue.getValue().getPerson().getFirstName()));
        participantLastName.setCellValueFactory(dataValue -> new SimpleStringProperty(dataValue.getValue().getPerson().getLastName()));

        gradeButton.disableProperty().bind(Bindings.isEmpty(maxValueField.textProperty()).or(Bindings.isEmpty(descriptionArea.textProperty())));

        participantsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        contextMenuShowTasks.disableProperty().bind(Bindings.size(participantsTable.getSelectionModel().getSelectedItems()).isNotEqualTo(1));
    }

    @FXML
    private void handleShowTasksAction(ActionEvent event) {
        stageManager.openModal(View.PERSON_TEAM_TASKS, participantsTable.getSelectionModel().getSelectedItem());
    }

    @Override
    public void setData(Team data) {
        super.setData(data);
        participantsTable.getItems().addAll(data.getPersonTeams());
    }
}
