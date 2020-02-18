package pl.edu.agh.pierogi.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private long teamID;
    private String name;
    @Column(name = "leader_id")
    private long leaderID;
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<PersonTeam> personTeams = new HashSet<>();

    public Team() {
    }

    public Team(String name) {
        this.name = name;
    }

    public Team(String name, Project project) {
        this.name = name;
        this.setProject(project);
    }


    public long getTeamID() {
        return teamID;
    }

    public void setTeamID(long teamID) {
        this.teamID = teamID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLeaderID() {
        return leaderID;
    }

    public void setLeaderID(long leaderID) {
        this.leaderID = leaderID;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        project.getTeams().add(this);
        this.project = project;
    }

    public void resetProject() {
        this.project.getTeams().remove(this);
        this.project = null;
    }

    public Set<PersonTeam> getPersonTeams() {
        return personTeams;
    }

    public PersonTeam addPerson(Person person) {
        PersonTeam personTeam = new PersonTeam(this, person);
        this.personTeams.add(personTeam);
        person.getPersonTeams().add(personTeam);
        return personTeam;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return teamID == team.teamID &&
                leaderID == team.leaderID &&
                Objects.equals(name, team.name) &&
                Objects.equals(project, team.project);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamID, name, leaderID, project);
    }

    @Override
    public String toString() {
        //"Team | name: " + name;
        return name;
    }
}
