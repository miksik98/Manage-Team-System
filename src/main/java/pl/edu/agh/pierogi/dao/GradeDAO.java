package pl.edu.agh.pierogi.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.pierogi.dao.repository.GradeRepository;
import pl.edu.agh.pierogi.model.Grade;
import pl.edu.agh.pierogi.model.Person;
import pl.edu.agh.pierogi.model.PersonTeam;
import pl.edu.agh.pierogi.model.Team;

import java.util.List;
import java.util.Optional;

@Service
public class GradeDAO implements GenericDAO<Grade> {

    @Autowired
    PersonTeamDAO personTeamDAO;
    @Autowired
    private GradeRepository gradeRepository;

    @Override
    public Grade save(Grade entity) {
        return gradeRepository.save(entity);
    }

    @Override
    public Grade update(Grade entity) {
        return gradeRepository.save(entity);
    }

    @Override
    public void delete(Grade entity) {
        gradeRepository.delete(entity);
    }

    @Override
    public void delete(Long id) {
        gradeRepository.deleteById(id);
    }

    @Override
    public void deleteInBatch(List<Grade> entities) {
        gradeRepository.deleteInBatch(entities);
    }

    @Override
    public Grade find(Long id) {
        return gradeRepository.getOne(id);
    }

    @Override
    public List<Grade> findAll() {
        return gradeRepository.findAll();
    }

    public Grade addGrade(Team team, Person person, Grade grade) {
        Optional<PersonTeam> personTeam = personTeamDAO.find(team, person);
        if (personTeam.isPresent()) {
            System.out.println(personTeam.get());
            personTeam.get().addGrade(grade);
            return this.save(grade);
        }
        return null;
    }
}
