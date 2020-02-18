package pl.edu.agh.pierogi.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.pierogi.dao.repository.PersonRepository;
import pl.edu.agh.pierogi.model.Person;
import pl.edu.agh.pierogi.model.Team;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PersonDAO implements GenericDAO<Person> {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonTeamDAO personTeamDAO;

    @Override
    public Person save(Person entity) {
        return personRepository.save(entity);
    }

    @Override
    public Person update(Person entity) {
        return personRepository.save(entity);
    }

    @Override
    public void delete(Person entity) {
        personTeamDAO.deleteByPerson(entity);
        personRepository.delete(entity);
    }

    @Override
    public void delete(Long id) {
        personRepository.deleteById(id);
    }

    @Override
    public void deleteInBatch(List<Person> entities) {
        entities.forEach(this::delete);
    }

    @Override
    public Person find(Long id) {
        return personRepository.getOne(id);
    }

    @Override
    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public List<Person> getPeopleNotFromTeam(Team entity) {
        Set<Long> teamMembers = entity.getPersonTeams().stream()
                .map(pt -> pt.getPerson().getPersonID())
                .collect(Collectors.toSet());

        return findAll().stream()
                .filter(p -> !teamMembers.contains(p.getPersonID()))
                .collect(Collectors.toList());
    }

    public List<Person> getPeopleFromTeam(Team entity) {
        return entity.getPersonTeams().stream()
                .map(pt -> pt.getPerson())
                .collect(Collectors.toList());
    }
}
