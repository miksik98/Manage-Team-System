package pl.edu.agh.pierogi.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Entity
@Table
public class PersonTeam implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_team_id")
    private long personTeamID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")
    private Person person;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;
    @OneToMany(mappedBy = "personTeam", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Grade> grade = new LinkedList<>();
    @OneToMany(mappedBy = "personTeam", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Task> task = new LinkedList<>();

    public PersonTeam() {
    }

    public PersonTeam(Team team, Person person) {
        setPerson(person);
        setTeam(team);
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
        person.getPersonTeams().add(this);
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
        team.getPersonTeams().add(this);
    }

    public void addTask(Task task) {
        task.setPersonTeam(this);
    }

    public List<Task> getTask() {
        return task;
    }

    public void addGrade(Grade grade) {
        grade.setPersonTeam(this);
    }

    public List<Grade> getGrade() {
        return grade;
    }

    public Double getPercent() {
        return this.grade
                .stream()
                .mapToDouble(Grade::getPercent)
                .average()
                .orElse(Double.NaN);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonTeam that = (PersonTeam) o;
        return personTeamID == that.personTeamID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(personTeamID);
    }
}
