package pl.edu.agh.pierogi.controller;

import com.itextpdf.text.DocumentException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pl.edu.agh.pierogi.config.StageManager;
import pl.edu.agh.pierogi.dao.PersonDAO;
import pl.edu.agh.pierogi.dao.ProjectDAO;
import pl.edu.agh.pierogi.dao.TeamDAO;
import pl.edu.agh.pierogi.model.Person;
import pl.edu.agh.pierogi.model.Project;
import pl.edu.agh.pierogi.model.Team;
import pl.edu.agh.pierogi.report.PersonWriter;
import pl.edu.agh.pierogi.report.ProjectWriter;
import pl.edu.agh.pierogi.report.ReportWriter;
import pl.edu.agh.pierogi.report.TeamWriter;
import pl.edu.agh.pierogi.view.View;

import java.io.FileNotFoundException;
import java.util.Optional;

@Controller
public class ReportsController {

    @Lazy
    @Autowired
    private StageManager stageManager;

    @Autowired
    private ProjectDAO projectDAO;

    @Autowired
    private TeamDAO teamDAO;

    @Autowired
    private PersonDAO personDAO;

    @FXML
    private Button openButton;

    @FXML
    private Button exportButton;

    @FXML
    private Button backButton;

    @FXML
    private ComboBox<Person> personCombo;

    @FXML
    private ComboBox<Team> teamCombo;

    @FXML
    private ComboBox<Project> projectCombo;

    @FXML
    private CheckBox personCheck;

    @FXML
    private CheckBox projectCheck;

    @FXML
    private CheckBox teamCheck;

    @FXML
    private TextField filenameField;

    @FXML
    private Label messageLabel;

    private Optional<Person> selectedPerson;

    private Optional<Team> selectedTeam;

    private Optional<Project> selectedProject;

    private ReportWriter reportWriter;


    @FXML
    private void initialize() {
        personCombo.setItems(FXCollections.observableList(personDAO.findAll()));
        teamCombo.setItems(FXCollections.observableList(teamDAO.findAll()));
        projectCombo.setItems(FXCollections.observableList(projectDAO.findAll()));

        clearAll();

        teamCombo.setVisible(false);
        personCombo.setVisible(false);
        projectCombo.setVisible(false);

        personCheck.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    personCombo.setVisible(true);
                    projectCheck.setSelected(false);
                    projectCombo.setVisible(false);
                    teamCheck.setSelected(false);
                    teamCombo.setVisible(false);
                    personCombo.getSelectionModel().clearSelection();
                    clearAll();
                } else {
                    personCombo.setVisible(true);
                }
            }
        });

        projectCheck.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    projectCombo.setVisible(true);
                    personCheck.setSelected(false);
                    personCombo.setVisible(false);
                    teamCheck.setSelected(false);
                    teamCombo.setVisible(false);
                    projectCombo.getSelectionModel().clearSelection();
                    clearAll();
                } else {
                    projectCombo.setVisible(false);
                }
            }
        });

        teamCheck.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    teamCombo.setVisible(true);
                    projectCheck.setSelected(false);
                    projectCombo.setVisible(false);
                    personCheck.setSelected(false);
                    personCombo.setVisible(false);
                    teamCombo.getSelectionModel().clearSelection();
                    clearAll();
                } else {
                    teamCombo.setVisible(false);
                }
            }
        });

        personCombo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Person>() {
            @Override
            public void changed(ObservableValue<? extends Person> observable, Person oldValue, Person newValue) {
                if (selectedPerson != null) {
                    selectedPerson = Optional.of(newValue);
                }
            }
        });

        projectCombo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Project>() {
            @Override
            public void changed(ObservableValue<? extends Project> observable, Project oldValue, Project newValue) {
                if (newValue != null) {
                    selectedProject = Optional.of(newValue);
                }
            }
        });

        teamCombo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Team>() {
            @Override
            public void changed(ObservableValue<? extends Team> observable, Team oldValue, Team newValue) {
                if (newValue != null) {
                    selectedTeam = Optional.of(newValue);
                }
            }
        });

    }

    @FXML
    protected void handleBackAction(ActionEvent event) {
        stageManager.switchScene(View.MAIN);
    }

    private void setMessageText(boolean isPositive, String message) {
        if (isPositive) {
            messageLabel.setTextFill(Color.GREEN);
            messageLabel.setText(message);
        } else {
            messageLabel.setTextFill(Color.RED);
            messageLabel.setText(message);
        }
    }

    private void exportData() {
        try {
            if (reportWriter.checkFileExistance()) {
                setMessageText(false, "File already exists");
                return;
            }
            if (reportWriter.exportData()) {
                setMessageText(true, "Data successfully exported");
            } else {
                setMessageText(false, "Failed to export data");
            }
        } catch (FileNotFoundException | DocumentException e) {
            setMessageText(false, "Failed to export data");
        }
    }

    private void clearAll() {
        selectedPerson = Optional.empty();
        selectedTeam = Optional.empty();
        selectedProject = Optional.empty();
        filenameField.setText("");
    }

    @FXML
    protected void handleExportAction(ActionEvent event) throws InterruptedException {
        if (filenameField.getText().isEmpty()) {

            setMessageText(false, "Filename not specified");

        } else if (projectCheck.isSelected() && selectedProject.isPresent()) {

            reportWriter = new ProjectWriter(selectedProject.get(), filenameField.getText());
            exportData();
            projectCheck.setSelected(false);
            projectCombo.setVisible(false);
            clearAll();

        } else if (personCheck.isSelected() && selectedPerson.isPresent()) {

            reportWriter = new PersonWriter(selectedPerson.get(), filenameField.getText());
            exportData();
            personCheck.setSelected(false);
            personCombo.setVisible(false);
            clearAll();

        } else if (teamCheck.isSelected() && selectedTeam.isPresent()) {

            reportWriter = new TeamWriter(selectedTeam.get(), filenameField.getText());
            exportData();
            teamCheck.setSelected(false);
            teamCombo.setVisible(false);
            clearAll();

        } else {

            setMessageText(false, "Data to export not specified");
        }
    }
}
