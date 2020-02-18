package pl.edu.agh.pierogi.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private long taskID;
    private String name;
    private String description;
    private boolean completed = false;
    @ManyToOne
    @JoinColumn(name = "person_team_id")
    private PersonTeam personTeam;

    public Task() {
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public long getTaskID() {
        return taskID;
    }

    public void setTaskID(long taskID) {
        this.taskID = taskID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PersonTeam getPersonTeam() {
        return personTeam;
    }

    public void setPersonTeam(PersonTeam personTeam) {
        personTeam.getTask().add(this);
        this.personTeam = personTeam;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return taskID == task.taskID &&
                Boolean.compare(completed, task.completed) == 0 &&
                Objects.equals(name, task.name) &&
                Objects.equals(description, task.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskID, completed, name, description);
    }

    @Override
    public String toString() {
        return this.name + " | Description: " + this.description + " | Completed: " + this.completed;
    }

}