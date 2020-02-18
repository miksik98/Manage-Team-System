package pl.edu.agh.pierogi.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pl.edu.agh.pierogi.config.StageManager;
import pl.edu.agh.pierogi.model.Person;
import pl.edu.agh.pierogi.model.PersonTeam;

@Controller
public class PersonTeamController extends BasicController<Person> {

    @Autowired
    @Lazy
    private StageManager stageManager;

    @FXML
    private TableView<PersonTeam> teamsTable;

    @FXML
    private TableColumn<PersonTeam, String> teamName;

    @FXML
    protected void initialize() {
        teamsTable.setItems(FXCollections.observableArrayList());

        teamName.setCellValueFactory(dataValue -> new SimpleStringProperty(dataValue.getValue().getTeam().getName()));
    }

    public void handleOkAction(ActionEvent event) {
        stageManager.closeModal();
    }

    @Override
    public void setData(Person data) {
        super.setData(data);

        teamsTable.getItems().addAll(data.getPersonTeams());
    }
}
