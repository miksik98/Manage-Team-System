package pl.edu.agh.pierogi.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pl.edu.agh.pierogi.config.StageManager;
import pl.edu.agh.pierogi.model.PersonTeam;
import pl.edu.agh.pierogi.model.Task;

@Controller
public class PersonTeamTasksController extends BasicController<PersonTeam> {

    @Lazy
    @Autowired
    private StageManager stageManager;

    @FXML
    private Button okButton;

    @FXML
    private TableView<Task> taskTable;

    @FXML
    private TableColumn<Task, String> taskNameColumn;

    @FXML
    private TableColumn<Task, String> descriptionColumn;

    @FXML
    private TableColumn<Task, String> isCompletedColumn;

    @FXML
    private Label personNameLabel;

    @FXML
    public void handleOkAction(ActionEvent event) {
        stageManager.closeModal();
    }

    @FXML
    protected void initialize() {
        taskNameColumn.setCellValueFactory(dataValue -> new SimpleStringProperty(dataValue.getValue().getName()));
        descriptionColumn.setCellValueFactory(dataValue -> new SimpleStringProperty(dataValue.getValue().getDescription()));
        isCompletedColumn.setCellValueFactory(dataValue -> new SimpleStringProperty(dataValue.getValue().isCompleted() ? "yes" : "no"));
    }

    @Override
    public void setData(PersonTeam data) {
        super.setData(data);
        personNameLabel.setText(data.getPerson().toString());

        for (Task task : data.getTask()) {
            taskTable.getItems().add(task);
        }
    }
}
