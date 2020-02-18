package pl.edu.agh.pierogi.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "grade_id")
    private long gradeID;
    private double value;
    private double maxValue;
    private String description;
    @ManyToOne
    @JoinColumn(name = "person_team_id")
    private PersonTeam personTeam;

    public Grade() {
    }

    public Grade(double value, double maxValue, String description) {
        this.value = value;
        this.maxValue = maxValue;
        this.description = description;
    }

    public Grade(double value, double maxValue, String description, PersonTeam personTeam) {
        this.value = value;
        this.maxValue = maxValue;
        this.description = description;
        this.setPersonTeam(personTeam);
    }

    public long getGradeID() {
        return gradeID;
    }

    public void setGradeID(long gradeID) {
        this.gradeID = gradeID;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
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
        personTeam.getGrade().add(this);
        this.personTeam = personTeam;
    }

    public Double getPercent() {
        return this.value / this.maxValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Grade grade = (Grade) o;
        return gradeID == grade.gradeID &&
                Double.compare(grade.value, value) == 0 &&
                Double.compare(grade.maxValue, maxValue) == 0 &&
                Objects.equals(description, grade.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gradeID, value, maxValue, description);
    }

    @Override
    public String toString() {
        return this.value + "/" + this.maxValue + " | Description: " + this.description;
    }
}
