package pl.edu.agh.pierogi.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private long projectID;
    private String topic;
    private String description;
    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Team> teams = new HashSet<>();

    public Project() {
    }

    public Project(String topic, String description) {
        this.topic = topic;
        this.description = description;
    }

    public long getProjectID() {
        return projectID;
    }

    public void setProjectID(long projectID) {
        this.projectID = projectID;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Team> getTeams() {
        return teams;
    }

    public void addTeam(Team team) {
        team.setProject(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return projectID == project.projectID &&
                Objects.equals(topic, project.topic) &&
                Objects.equals(description, project.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectID, topic, description);
    }

    @Override
    public String toString() {
        //"Project | topic: " + topic + " | description: " + description;
        return topic;
    }
}
