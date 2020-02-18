package pl.edu.agh.pierogi.rank;

import pl.edu.agh.pierogi.model.Person;
import pl.edu.agh.pierogi.model.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LesserThanCriteria implements Criteria {
    Double value;
    Team team;

    public LesserThanCriteria(Double value, Team team) {
        this.value = value;
        this.team = team;
    }

    @Override
    public List<Person> meetCriteria(List<Person> people) {
        if (value == null) return people;
        List<Person> peopleWithPointsLesserThanValue = new ArrayList<>();
        for (Person person : people) {
            Optional<Double> sum = person.getGradesSum(team);
            if (sum.isPresent() && sum.get() < value) {
                peopleWithPointsLesserThanValue.add(person);
            }
        }
        return peopleWithPointsLesserThanValue;
    }
}
