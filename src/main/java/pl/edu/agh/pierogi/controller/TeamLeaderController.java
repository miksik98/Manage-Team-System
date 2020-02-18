package pl.edu.agh.pierogi.controller;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pl.edu.agh.pierogi.config.StageManager;
import pl.edu.agh.pierogi.model.Person;
import pl.edu.agh.pierogi.model.PersonTeam;
import pl.edu.agh.pierogi.model.Team;
import pl.edu.agh.pierogi.view.View;

import java.util.Optional;

@Controller
public class TeamLeaderController extends BasicController<Team> {

    @FXML
    public TableView<PersonTeam> participantsTable;
    @FXML
    public TableColumn<PersonTeam, String> participantFirstName;
    @FXML
    public TableColumn<PersonTeam, String> participantLastName;
    @FXML
    public Button setButton;
    @Autowired
    @Lazy
    private StageManager stageManager;

    @FXML
    protected void initialize() {
        participantFirstName.setCellValueFactory(dataValue -> new SimpleStringProperty(dataValue.getValue().getPerson().getFirstName()));
        participantLastName.setCellValueFactory(dataValue -> new SimpleStringProperty(dataValue.getValue().getPerson().getLastName()));

        setButton.disableProperty().bind(Bindings.size(participantsTable.getSelectionModel().getSelectedItems()).isNotEqualTo(1));
    }

    @Override
    public void setData(Team data) {
        super.setData(data);

        participantsTable.getItems().addAll(data.getPersonTeams());
    }

    public void handleSetAction(ActionEvent event) {
        Optional<Person> leader = data.getPersonTeams().stream()
                .map(pt -> pt.getPerson())
                .filter(p -> p.getPersonID() == data.getLeaderID())
                .findFirst();

        if (leader.isPresent() && !stageManager.openModal(View.CONFIRMATION, data)) return;

        Person selectedPerson = participantsTable.getSelectionModel().getSelectedItem().getPerson();
        data.setLeaderID(selectedPerson.getPersonID());

        stageManager.closeModal();
    }
}
