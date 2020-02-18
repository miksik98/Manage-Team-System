package pl.edu.agh.pierogi.rank;

import pl.edu.agh.pierogi.model.Person;

import java.util.List;

public class AndCriteria implements Criteria {
    private Criteria criteria1;
    private Criteria criteria2;

    public AndCriteria(Criteria criteria1, Criteria criteria2) {
        this.criteria1 = criteria1;
        this.criteria2 = criteria2;
    }

    @Override
    public List<Person> meetCriteria(List<Person> people) {
        List<Person> peopleWhichMeetCriteria1 = criteria1.meetCriteria(people);
        return criteria2.meetCriteria(peopleWhichMeetCriteria1);
    }
}
