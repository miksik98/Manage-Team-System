package pl.edu.agh.pierogi.view;

import java.util.ResourceBundle;

public enum View {

    PEOPLE {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("people.title");
        }

        @Override
        public String getFxmlFile() {
            return "/view/Person/All.fxml";
        }
    },

    CREATE_PERSON {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("createPerson.title");
        }

        @Override
        public String getFxmlFile() {
            return "/view/Person/Add.fxml";
        }
    },

    TEAMS {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("teams.title");
        }

        @Override
        public String getFxmlFile() {
            return "/view/Team/All.fxml";
        }
    },

    SET_LEADER {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("teams.title");
        }

        @Override
        public String getFxmlFile() {
            return "/view/Team/SetLeader.fxml";
        }
    },

    SET_PROJECT {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("teams.title");
        }

        @Override
        public String getFxmlFile() {
            return "/view/Team/SetProject.fxml";
        }
    },

    PERSON_TEAMS {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("personTeams.title");
        }

        @Override
        public String getFxmlFile() {
            return "/view/Person/Teams.fxml";
        }
    },

    MANAGE_TASKS {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("manageTasks.title");
        }

        @Override
        public String getFxmlFile() {
            return "/view/Person/ManageTasks.fxml";
        }
    },

    PROJECTS {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("projects.title");
        }

        @Override
        public String getFxmlFile() {
            return "/view/Project/All.fxml";
        }
    },

    PROJECT_TEAMS {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("projectTeams.title");
        }

        @Override
        public String getFxmlFile() {
            return "/view/Project/Teams.fxml";
        }
    },

    CREATE_PROJECT {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("createProject.title");
        }

        @Override
        public String getFxmlFile() {
            return "/view/Project/Add.fxml";
        }
    },

    CREATE_TEAM {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("createTeam.title");
        }

        @Override
        public String getFxmlFile() {
            return "/view/Team/Add.fxml";
        }
    },

    TEAM_DETAILS {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("teamDetails.title");
        }

        @Override
        public String getFxmlFile() {
            return "/view/Team/Details.fxml";
        }
    },

    GRADE {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("grade.title");
        }

        @Override
        public String getFxmlFile() {
            return "/view/Team/Grading/Grade.fxml";
        }
    },

    TEAM_GRADES {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("teamGrades.title");
        }

        @Override
        public String getFxmlFile() {
            return "/view/Team/Grading/TeamGrades.fxml";
        }
    },

    NEW_TASK {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("newTask.title");
        }

        @Override
        public String getFxmlFile() {
            return "/view/Team/NewTask.fxml";
        }
    },

    CONFIRMATION {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("main.title");
        }

        @Override
        public String getFxmlFile() {
            return "/view/Confirmation.fxml";
        }
    },

    ADD_MEMBERS {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("addMembers.title");
        }

        @Override
        public String getFxmlFile() {
            return "/view/Team/AddMembers.fxml";
        }
    },

    TASK_DESCRIPTION {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("taskDescription.title");
        }

        @Override
        public String getFxmlFile() {
            return "/view/Task/TaskDescription.fxml";
        }
    },

    RANK {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("rank.title");
        }

        @Override
        public String getFxmlFile() {
            return "/view/Ranking.fxml";
        }
    },

    MAIN {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("main.title");
        }

        @Override
        public String getFxmlFile() {
            return "/view/Main.fxml";
        }
    },

    PERSON_TEAM_TASKS {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("personTeamTasks.title");
        }

        @Override
        public String getFxmlFile() {
            return "/view/Task/PersonTeamTasks.fxml";
        }
    },

    REPORT {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("report.title");
        }

        @Override
        public String getFxmlFile() {
            return "/view/Reports.fxml";
        }
    },

    EMAIL {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("email.title");
        }

        @Override
        public String getFxmlFile() {
            return "/view/Emails.fxml";
        }
    };

    public abstract String getTitle();

    public abstract String getFxmlFile();

    String getStringFromResourceBundle(String key) {
        return ResourceBundle.getBundle("Bundle").getString(key);
    }

}