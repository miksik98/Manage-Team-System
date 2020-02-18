package pl.edu.agh.pierogi.rank;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import pl.edu.agh.pierogi.model.Person;
import pl.edu.agh.pierogi.model.PersonTeam;
import pl.edu.agh.pierogi.model.Team;

import java.util.Set;
import java.util.stream.Collectors;

public class PercentStrategy implements ValueComputingStrategy {

    @Override
    public ObservableValue<Double> getValue(Person person, Team team) {
        Set<PersonTeam> personTeams = person.getPersonTeams();
        if (team != null) {
            personTeams = personTeams.stream().filter(personTeam -> personTeam.getTeam().getTeamID() == team.getTeamID()).collect(Collectors.toSet());
        }
        int ptSize = personTeams.size();
        return personTeams.stream().map(PersonTeam::getPercent).reduce((sum, current) -> sum += current).map(sum -> sum / ptSize).<javafx.beans.value.ObservableValue<Double>>map(val -> {
            int v = (int) (val * 10000);
            val = v / 100.0;
            return new SimpleObjectProperty<>(val);
        }).orElseGet(() -> new SimpleObjectProperty(0.0));
    }
}
