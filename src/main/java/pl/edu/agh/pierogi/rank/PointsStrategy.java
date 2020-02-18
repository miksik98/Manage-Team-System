package pl.edu.agh.pierogi.rank;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import pl.edu.agh.pierogi.model.Person;
import pl.edu.agh.pierogi.model.Team;

public class PointsStrategy implements ValueComputingStrategy {
    @Override
    public ObservableValue<Double> getValue(Person person, Team team) {
        return person.getGradesSum(team).<javafx.beans.value.ObservableValue<Double>>map(SimpleObjectProperty::new).orElseGet(() -> new SimpleObjectProperty<>(0.0));
    }
}
