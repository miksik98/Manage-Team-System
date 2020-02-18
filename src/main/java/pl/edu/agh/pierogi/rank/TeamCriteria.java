package pl.edu.agh.pierogi.rank;

import pl.edu.agh.pierogi.model.Person;
import pl.edu.agh.pierogi.model.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TeamCriteria implements Criteria {
    Team team;

    public TeamCriteria(Team team) {
        this.team = team;
    }

    @Override
    public List<Person> meetCriteria(List<Person> people) {
        List<Person> peopleFromTeam = new ArrayList<>();
        if (team == null) return people;
        for (Person person : people) {
            if (person.getPersonTeams().stream().map(personTeam -> personTeam.getTeam().getTeamID()).collect(Collectors.toList()).contains(team.getTeamID())) {
                peopleFromTeam.add(person);
            }
        }
        return peopleFromTeam;
    }
}
