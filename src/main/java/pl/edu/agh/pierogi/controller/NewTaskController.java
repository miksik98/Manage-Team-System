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
import pl.edu.agh.pierogi.model.PersonTeam;
import pl.edu.agh.pierogi.model.Task;
import pl.edu.agh.pierogi.model.Team;


@Controller
public class NewTaskController extends BasicController<Team> {
    @FXML
    public TableView<PersonTeam> participantsTable;
    @FXML
    public TableColumn<PersonTeam, String> participantFirstName;
    @FXML
    public TableColumn<PersonTeam, String> participantLastName;
    @FXML
    public Button createButton;
    @FXML
    public TextField nameField;
    @FXML
    public TextArea descriptionField;
    @Autowired
    private TaskDAO taskDAO;
    @Lazy
    @Autowired
    private StageManager stageManager;

    @FXML
    protected void initialize() {
        participantFirstName.setCellValueFactory(dataValue -> new SimpleStringProperty(dataValue.getValue().getPerson().getFirstName()));
        participantLastName.setCellValueFactory(dataValue -> new SimpleStringProperty(dataValue.getValue().getPerson().getLastName()));

        createButton.disableProperty().bind(Bindings.size(participantsTable.getSelectionModel().getSelectedItems()).isNotEqualTo(1).
                or(Bindings.createBooleanBinding(() -> nameField.getText().trim().isEmpty(), nameField.textProperty())));
    }

    public void handleCreateAction(ActionEvent e) {
        PersonTeam person = participantsTable.getSelectionModel().getSelectedItem();
        Task task = new Task(nameField.getText(), descriptionField.getText());
        person.addTask(task);
        taskDAO.save(task);

        stageManager.closeModal();
    }

    @Override
    public void setData(Team data) {
        super.setData(data);
        participantsTable.getItems().addAll(data.getPersonTeams());
    }
}
