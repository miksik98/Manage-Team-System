package pl.edu.agh.pierogi.rank;

import javafx.beans.value.ObservableValue;
import pl.edu.agh.pierogi.model.Person;
import pl.edu.agh.pierogi.model.Team;

public interface ValueComputingStrategy {
    ObservableValue<Double> getValue(Person person, Team team);
}
