package pl.edu.agh.pierogi.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.pierogi.dao.repository.PersonRepository;
import pl.edu.agh.pierogi.dao.repository.TeamRepository;
import pl.edu.agh.pierogi.model.Person;
import pl.edu.agh.pierogi.model.Project;
import pl.edu.agh.pierogi.model.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Service
public class TeamDAO implements GenericDAO<Team> {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonTeamDAO personTeamDAO;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Team save(Team entity) {
        return teamRepository.save(entity);
    }

    @Override
    public Team update(Team entity) {
        return teamRepository.save(entity);
    }

    @Override
    public void delete(Team entity) {
        personTeamDAO.deleteByTeam(entity);
        teamRepository.delete(entity);
    }

    @Override
    public void delete(Long id) {
        teamRepository.deleteById(id);
    }

    @Override
    public void deleteInBatch(List<Team> entities) {
        entities.stream()
                .forEach(this::delete);
    }

    @Override
    public Team find(Long id) {
        return teamRepository.getOne(id);
    }

    @Override
    public List<Team> findAll() {
        return teamRepository.findAll();
    }

    public Optional<Team> findByName(String name) {
        List<Team> teams = findAll();
        return teams.stream()
                .filter(t -> t.getName().equals(name))
                .findFirst();
    }

    public Team addLeader(Team team, Person leader) {
        team.setLeaderID(leader.getPersonID());
        return teamRepository.save(team);
    }

    public Person getLeader(Team team) {
        return personRepository.getOne(team.getLeaderID());
    }

    public void addProject(Team team, Project project) {
        team.setProject(project);
        teamRepository.save(team);
    }
}
