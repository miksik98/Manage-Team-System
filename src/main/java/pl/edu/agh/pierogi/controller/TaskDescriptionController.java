package pl.edu.agh.pierogi.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pl.edu.agh.pierogi.config.StageManager;
import pl.edu.agh.pierogi.model.Task;

@Controller
public class TaskDescriptionController extends BasicController<Task> {
    @Lazy
    @Autowired
    private StageManager stageManager;

    @FXML
    private Label descriptionLabel;

    @FXML
    private void handleOkAction(ActionEvent event) {
        stageManager.closeModal();
    }

    @Override
    public void setData(Task data) {
        super.setData(data);
        String description = data.getDescription();
        if (!description.trim().isEmpty()) {
            descriptionLabel.setText(description);
        } else {
            descriptionLabel.setText("NO DESCRIPTION");
        }
    }

}
