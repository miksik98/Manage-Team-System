package pl.edu.agh.pierogi.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pl.edu.agh.pierogi.PierogiApplication;
import pl.edu.agh.pierogi.commands.SaveTeamCommand;
import pl.edu.agh.pierogi.config.StageManager;
import pl.edu.agh.pierogi.dao.TeamDAO;
import pl.edu.agh.pierogi.model.Team;

@Controller
public class TeamAddController extends BasicController<Team> {
    @Autowired
    private TeamDAO teamDAO;

    @Lazy
    @Autowired
    private StageManager stageManager;

    @FXML
    private Button okButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Label errorLabel;

    @FXML
    private TextField nameField;

    @FXML
    private void handleOkAction(ActionEvent event) {
        if (isInputValid()) {
            updateModel();
            SaveTeamCommand saveTeamCommand = new SaveTeamCommand(teamDAO, data);
            PierogiApplication.teamsCommandRegistry.executeCommand(saveTeamCommand);
            stageManager.closeModal();
        } else {
            errorLabel.setText("Insert correct data");
        }
    }

    private void updateModel() {
        data.setName(nameField.getText());
    }

    private boolean isInputValid() {
        return !nameField.getText().isEmpty()
                && teamDAO.findByName(nameField.getText()).isEmpty();
    }

    @FXML
    private void handleCancelAction() {
        stageManager.closeModal();
    }
}
