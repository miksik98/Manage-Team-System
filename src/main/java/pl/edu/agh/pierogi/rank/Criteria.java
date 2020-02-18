package pl.edu.agh.pierogi.rank;

import pl.edu.agh.pierogi.model.Person;

import java.util.List;

public interface Criteria {
    List<Person> meetCriteria(List<Person> people);
}
