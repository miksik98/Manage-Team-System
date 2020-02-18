package pl.edu.agh.pierogi.controller;

import com.google.gson.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.bouncycastle.asn1.x509.sigi.PersonalData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pl.edu.agh.pierogi.config.StageManager;
import pl.edu.agh.pierogi.dao.PersonDAO;
import pl.edu.agh.pierogi.dao.ProjectDAO;
import pl.edu.agh.pierogi.model.Person;
import pl.edu.agh.pierogi.model.Project;
import pl.edu.agh.pierogi.view.View;

import java.io.*;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
public class MainController {
    public Button importButton;
    @Autowired
    private PersonDAO personDAO;

    @Autowired
    private ProjectDAO projectDAO;

    public VBox mainMenu;
    @Lazy
    @Autowired
    private StageManager stageManager;

    @FXML
    private Button peopleButton;

    @FXML
    private Button projectsButton;

    @FXML
    private Button teamsButton;

    @FXML
    private Button reportsButton;

    @FXML
    private void handlePeopleAction(ActionEvent e) {
        stageManager.switchScene(View.PEOPLE);
    }

    @FXML
    private void handleProjectsAction(ActionEvent e) {
        stageManager.switchScene(View.PROJECTS);
    }

    @FXML
    private void handleTeamsAction(ActionEvent e) {
        stageManager.switchScene(View.TEAMS);
    }

    @FXML
    protected void handleRankAction(ActionEvent actionEvent) {
        stageManager.switchScene(View.RANK);
    }

    @FXML
    protected void handleReportsAction(ActionEvent actionEvent) {
        stageManager.switchScene(View.REPORT);
    }

    @FXML
    protected void handleEmailsAction(ActionEvent actionEvent) {
        stageManager.switchScene(View.EMAIL);
    }

    public void handleImportAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON files", "*.json"));
        File selectedFile = fileChooser.showOpenDialog(stageManager.getPrimaryStage());
        StringBuilder json = new StringBuilder("");

        try {
            BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
            String strCurrentLine;
            while ((strCurrentLine = reader.readLine()) != null) {
                json.append(strCurrentLine);
            }

            Gson gson = new Gson();
            JsonElement[] elements = gson.fromJson(json.toString(), JsonElement[].class);

            for (JsonElement e : elements) {
                JsonObject em = e.getAsJsonObject();
                Set<Map.Entry<String, JsonElement>> entries = em.entrySet();
                for (Map.Entry<String, JsonElement> entry: entries) {
                    String collectionName = entry.getKey();
                    if (collectionName.equalsIgnoreCase("Person")) {
                        JsonArray peopleJson = em.getAsJsonArray("Person");
                        for (JsonElement obj : peopleJson) {
                            Person person = gson.fromJson(obj, Person.class);
                            personDAO.save(person);
                        }
                    } else if (collectionName.equalsIgnoreCase("Project")) {
                        JsonArray projectsArray = em.getAsJsonArray("Project");
                        for (JsonElement obj : projectsArray) {
                            Project project = gson.fromJson(obj, Project.class);
                            projectDAO.save(project);
                        }
                    }
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
