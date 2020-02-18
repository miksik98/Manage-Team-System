package pl.edu.agh.pierogi.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
import pl.edu.agh.pierogi.view.View;
import pl.edu.agh.pierogi.visitor.MailSenderVisitor;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Controller
public class EmailController {

    @Lazy
    @Autowired
    private StageManager stageManager;

    @Autowired
    private ProjectDAO projectDAO;

    @Autowired
    private TeamDAO teamDAO;

    @Autowired
    private PersonDAO personDAO;

    @Autowired
    private MailSenderVisitor mailSenderVisitor;

    @FXML
    private ComboBox<Project> projectCombo;

    @FXML
    private ComboBox<Team> teamCombo;

    @FXML
    private CheckBox projectCheck;

    @FXML
    private CheckBox projectAllCheck;

    @FXML
    private CheckBox projectLeaderCheck;

    @FXML
    private CheckBox teamCheck;

    @FXML
    private CheckBox teamAllCheck;

    @FXML
    private CheckBox teamLeaderCheck;

    @FXML
    private CheckBox differentGroupCheck;

    @FXML
    private Label messageLabel;

    @FXML
    private TableView<Person> differentGroupTableView;

    private Optional<Team> selectedTeam;

    private Optional<Project> selectedProject;

    @FXML
    private TextArea messageText;

    @FXML
    private TableColumn<Person, String> firstNameColumn;

    @FXML
    private TableColumn<Person, String> surnameColumn;

    @FXML
    private TableColumn<Person, String> emailColumn;

    private void reset() {
        projectLeaderCheck.setSelected(false);
        projectAllCheck.setSelected(false);
        projectLeaderCheck.setSelected(false);
        teamAllCheck.setSelected(false);
        teamLeaderCheck.setSelected(false);
        projectAllCheck.setDisable(true);
        projectLeaderCheck.setDisable(true);
        teamAllCheck.setDisable(true);
        teamLeaderCheck.setDisable(true);
        projectCombo.setDisable(true);
        teamCombo.setDisable(true);
        differentGroupTableView.setDisable(true);
    }

    @FXML
    private void initialize() {
        reset();

        this.teamCombo.setItems(FXCollections.observableArrayList(teamDAO.findAll()));
        this.projectCombo.setItems(FXCollections.observableArrayList(projectDAO.findAll()));
        this.differentGroupTableView.setItems(FXCollections.observableArrayList(personDAO.findAll()));

        differentGroupTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        projectCheck.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                reset();
                if (newValue) {
                    teamCheck.setSelected(false);
                    differentGroupCheck.setSelected(false);
                    projectAllCheck.setSelected(true);
                    projectAllCheck.setDisable(false);
                    projectLeaderCheck.setDisable(false);
                    projectCombo.setDisable(false);
                }
            }
        });


        teamCheck.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                reset();
                if (newValue) {
                    projectCheck.setSelected(false);
                    differentGroupCheck.setSelected(false);
                    teamAllCheck.setSelected(true);
                    teamLeaderCheck.setDisable(false);
                    teamAllCheck.setDisable(false);
                    teamCombo.setDisable(false);
                }
            }
        });

        differentGroupCheck.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                reset();
                if (newValue) {
                    projectCheck.setSelected(false);
                    teamCheck.setSelected(false);
                    differentGroupTableView.setDisable(false);
                }
            }
        });

        projectAllCheck.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!oldValue) {
                    projectLeaderCheck.setSelected(false);
                }
            }
        });

        projectLeaderCheck.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!oldValue) {
                    projectAllCheck.setSelected(false);
                }
            }
        });

        teamAllCheck.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!oldValue) {
                    teamLeaderCheck.setSelected(false);
                }
            }
        });

        teamLeaderCheck.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!oldValue) {
                    teamAllCheck.setSelected(false);
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

        projectCombo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Project>() {
            @Override
            public void changed(ObservableValue<? extends Project> observable, Project oldValue, Project newValue) {
                if (newValue != null) {
                    selectedProject = Optional.of(newValue);
                }
            }
        });

    }

    @FXML
    protected void handleBackAction(ActionEvent event) {
        stageManager.switchScene(View.MAIN);
    }

    @FXML
    protected void handleSendAction(ActionEvent event) {
        List<Person> personList = new LinkedList<>();
        if (teamCheck.isSelected()) {
            SingleSelectionModel<Team> model = teamCombo.getSelectionModel();
            if (model.isEmpty()) return;
            if (teamLeaderCheck.isSelected()) {
                personList.add(personDAO.find(model.getSelectedItem().getLeaderID()));
            } else {
                model.getSelectedItem().getPersonTeams().forEach(x -> personList.add(x.getPerson()));
            }
        } else if (projectCheck.isSelected()) {
            SingleSelectionModel<Project> model = projectCombo.getSelectionModel();
            if (model.isEmpty()) return;
            if (projectLeaderCheck.isSelected()) {
                model.getSelectedItem().getTeams()
                        .forEach(t -> personList.add(personDAO.find(t.getLeaderID())));
            } else {
                model.getSelectedItem().getTeams()
                        .forEach(t -> t.getPersonTeams()
                                .forEach(pt -> personList.add(pt.getPerson())));
            }

        } else if (differentGroupCheck.isSelected()) {
            personList.addAll(differentGroupTableView.getSelectionModel().getSelectedItems());
        }
        mailSenderVisitor.sendMails(personList, "Auto generated message", messageText.getText());
        reset();
        messageText.setText("");
        setMessageText(true, "Message was sent to " + personList.size() + " people.");

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

}

