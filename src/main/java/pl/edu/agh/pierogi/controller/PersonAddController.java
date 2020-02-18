package pl.edu.agh.pierogi.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pl.edu.agh.pierogi.PierogiApplication;
import pl.edu.agh.pierogi.commands.SavePersonCommand;
import pl.edu.agh.pierogi.config.StageManager;
import pl.edu.agh.pierogi.dao.PersonDAO;
import pl.edu.agh.pierogi.model.Person;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class PersonAddController extends BasicController<Person> {
    @Autowired
    @Lazy
    private StageManager stageManager;

    @Autowired
    private PersonDAO personDAO;

    @FXML
    private TextField firstnameTextField;

    @FXML
    private TextField lastnameTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private Label errorLabel;

    public void handleOkAction(ActionEvent event) {
        if (isInputValid()) {
            updateModel();
            SavePersonCommand savePersonCommand = new SavePersonCommand(personDAO, data);
            PierogiApplication.peopleCommandRegistry.executeCommand(savePersonCommand);
            stageManager.closeModal();
        } else {
            errorLabel.setText("Insert correct data");
        }
    }

    private void updateModel() {
        data.setFirstName(firstnameTextField.getText());
        data.setLastName(lastnameTextField.getText());
        data.setEmail(emailTextField.getText());
    }

    private boolean isInputValid() {
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(emailTextField.getText());
        return !firstnameTextField.getText().isEmpty() && !lastnameTextField.getText().isEmpty() && matcher.matches();
    }

    public void handleCancelAction(ActionEvent event) {
        stageManager.closeModal();
    }
}
