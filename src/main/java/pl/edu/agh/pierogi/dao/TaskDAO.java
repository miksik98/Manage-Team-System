package pl.edu.agh.pierogi.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.pierogi.dao.repository.TaskRepository;
import pl.edu.agh.pierogi.model.Person;
import pl.edu.agh.pierogi.model.PersonTeam;
import pl.edu.agh.pierogi.model.Task;
import pl.edu.agh.pierogi.model.Team;

import java.util.List;
import java.util.Optional;

@Service
public class TaskDAO implements GenericDAO<Task> {

    @Autowired
    PersonTeamDAO personTeamDAO;
    @Autowired
    private TaskRepository taskRepository;

    @Override
    public Task save(Task entity) {
        return taskRepository.save(entity);
    }

    @Override
    public Task update(Task entity) {
        return taskRepository.save(entity);
    }

    @Override
    public void delete(Task entity) {
        taskRepository.delete(entity);
    }

    @Override
    public void delete(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public void deleteInBatch(List<Task> entities) {
        entities.forEach(this::delete);
    }

    @Override
    public Task find(Long id) {
        return taskRepository.getOne(id);
    }

    @Override
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public Task addTask(Team team, Person person, Task task) {
        Optional<PersonTeam> personTeam = personTeamDAO.find(team, person);
        if (personTeam.isPresent()) {
            System.out.println(personTeam.get());
            personTeam.get().addTask(task);
            return this.save(task);
        }
        return null;
    }
}
