package pl.edu.agh.pierogi.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pl.edu.agh.pierogi.PierogiApplication;
import pl.edu.agh.pierogi.commands.SaveProjectCommand;
import pl.edu.agh.pierogi.config.StageManager;
import pl.edu.agh.pierogi.dao.ProjectDAO;
import pl.edu.agh.pierogi.model.Project;

@Controller
public class ProjectAddController extends BasicController<Project> {
    @Autowired
    private ProjectDAO projectDAO;

    @Lazy
    @Autowired
    private StageManager stageManager;

    @FXML
    private Button okButton;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField topicField;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private Label errorLabel;

    @FXML
    private void handleOkAction(ActionEvent event) {
        if (isInputValid()) {
            updateModel();
            SaveProjectCommand saveProjectCommand = new SaveProjectCommand(projectDAO, data);
            PierogiApplication.projectsCommandRegistry.executeCommand(saveProjectCommand);
            stageManager.closeModal();
        } else {
            errorLabel.setText("Insert correct data");
        }
    }

    private void updateModel() {
        data.setTopic(topicField.getText());
        data.setDescription(descriptionArea.getText());
    }

    private boolean isInputValid() {
        return !topicField.getText().isEmpty()
                && !descriptionArea.getText().isEmpty()
                && projectDAO.findByTopic(topicField.getText()).isEmpty();
    }

    @FXML
    private void handleCancelAction() {
        stageManager.closeModal();
    }
}
