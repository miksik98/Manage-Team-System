package pl.edu.agh.pierogi.rank;

import pl.edu.agh.pierogi.model.Person;

import java.util.List;

public class OrCriteria implements Criteria {
    private Criteria criteria1;
    private Criteria criteria2;

    public OrCriteria(Criteria criteria1, Criteria criteria2) {
        this.criteria1 = criteria1;
        this.criteria2 = criteria2;
    }

    @Override
    public List<Person> meetCriteria(List<Person> people) {
        List<Person> peopleWhichMeetCriteria1 = criteria1.meetCriteria(people);
        List<Person> peopleWhichMeetCriteria2 = criteria2.meetCriteria(people);
        for (Person person : peopleWhichMeetCriteria2) {
            if (!peopleWhichMeetCriteria1.contains(person))
                peopleWhichMeetCriteria1.add(person);
        }
        return peopleWhichMeetCriteria1;
    }
}
