package pl.edu.agh.pierogi.controller;

import javafx.event.ActionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pl.edu.agh.pierogi.config.StageManager;

@Controller
public class ConfirmationController extends BasicController {
    @Autowired
    @Lazy
    private StageManager stageManager;

    public void initialize() {
        Approved = false;
    }

    public void handleYesAction(ActionEvent event) {
        Approved = true;
        stageManager.closeModal();
    }

    public void handleNoAction(ActionEvent event) {
        Approved = false;
        stageManager.closeModal();
    }
}
