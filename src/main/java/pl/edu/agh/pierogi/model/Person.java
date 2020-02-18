package pl.edu.agh.pierogi.model;

import pl.edu.agh.pierogi.visitor.Visitable;
import pl.edu.agh.pierogi.visitor.Visitor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Entity
@Table
public class Person implements Visitable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id")
    private long personID;
    private String firstName;
    private String lastName;
    private String email;
    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<PersonTeam> personTeams = new HashSet<>();

    public Person() {
    }

    public Person(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public long getPersonID() {
        return personID;
    }

    public void setPersonID(long personID) {
        this.personID = personID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<PersonTeam> getPersonTeams() {
        return personTeams;
    }

    public void setPersonTeams(Set<PersonTeam> personTeams) {
        this.personTeams = personTeams;
    }

    public PersonTeam addTeam(Team team) {
        PersonTeam personTeam = new PersonTeam(team, this);
        this.personTeams.add(personTeam);
        team.getPersonTeams().add(personTeam);
        return personTeam;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Person)) return false;
        Person person = (Person) o;
        return personID == person.getPersonID() &&
                Objects.equals(firstName, person.getFirstName()) &&
                Objects.equals(lastName, person.getLastName()) &&
                Objects.equals(email, person.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(personID, firstName, lastName, email);
    }

    @Override
    public String toString() {
        //firstName + " " + lastName + " with email " + email;
        return firstName + " " + lastName;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public Optional<Double> getGradesSum(Team team) {
        Optional<Double> reduce = this.getPersonTeams().stream().filter(personTeam -> {
            if (team != null)
                return personTeam.getTeam().getTeamID() == team.getTeamID();
            else
                return true;
        }).map(personTeam -> {
            double gradesSum = 0.0;
            for (Grade grade : personTeam.getGrade()) {
                gradesSum += grade.getValue();
            }
            return gradesSum;
        }).reduce((sum, current) -> sum += current);

        return reduce;
    }
}
