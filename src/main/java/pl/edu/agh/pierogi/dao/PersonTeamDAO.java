package pl.edu.agh.pierogi.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.pierogi.dao.repository.PersonTeamRepository;
import pl.edu.agh.pierogi.model.Grade;
import pl.edu.agh.pierogi.model.Person;
import pl.edu.agh.pierogi.model.PersonTeam;
import pl.edu.agh.pierogi.model.Team;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class PersonTeamDAO implements GenericDAO<PersonTeam> {

    @Autowired
    PersonTeamRepository personTeamRepository;

    @Autowired
    GradeDAO gradeDAO;

    @Override
    public PersonTeam save(PersonTeam entity) {
        return personTeamRepository.save(entity);
    }

    @Override
    public PersonTeam update(PersonTeam entity) {
        return personTeamRepository.save(entity);
    }

    @Override
    public void delete(PersonTeam entity) {
        personTeamRepository.delete(entity);
    }

    @Override
    public void delete(Long id) {
        personTeamRepository.deleteById(id);
    }

    @Override
    public void deleteInBatch(List<PersonTeam> entities) {
        personTeamRepository.deleteInBatch(entities);
    }

    @Override
    public PersonTeam find(Long id) {
        return personTeamRepository.getOne(id);
    }

    @Override
    public List<PersonTeam> findAll() {
        return personTeamRepository.findAll();
    }

    public Optional<PersonTeam> find(Team team, Person person) {
        return personTeamRepository.findAll().stream().filter(
                pt -> pt.getTeam().equals(team) && pt.getPerson().equals(person)).findFirst();

    }

    public PersonTeam savePersonTeam(Team team, Person person) {
        PersonTeam personTeam = new PersonTeam(team, person);
        return personTeamRepository.save(personTeam);
    }

    public List<Grade> getAllGrades(Team team, Person person) {
        List<Grade> result = new LinkedList<>();
        Optional<PersonTeam> pt = this.find(team, person);
        pt.ifPresent(personTeam -> result.addAll(personTeam.getGrade()));
        return result;
    }

    public Double calculatePercent(Team team, Person person) {
        Optional<PersonTeam> pt = this.find(team, person);
        if (pt.isPresent()) {
            return pt.get().getPercent();
        }
        return Double.NaN;
    }

    public void deleteByTeam(Team team) {
        findAll().stream()
                .filter(pt -> pt.getTeam().equals(team))
                .forEach(this::delete);
    }

    public void deleteByPerson(Person person) {
        findAll().stream()
                .filter(pt -> pt.getPerson().equals(person))
                .forEach(this::delete);
    }

}
