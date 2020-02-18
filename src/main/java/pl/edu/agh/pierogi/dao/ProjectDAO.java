package pl.edu.agh.pierogi.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.pierogi.dao.repository.ProjectRepository;
import pl.edu.agh.pierogi.model.Project;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectDAO implements GenericDAO<Project> {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TeamDAO teamDAO;

    @Override
    public Project save(Project entity) {
        return projectRepository.save(entity);
    }

    @Override
    public Project update(Project entity) {
        return projectRepository.save(entity);
    }

    @Override
    public void delete(Project entity) {
        entity.getTeams().forEach(team -> {
            team.resetProject();
            teamDAO.save(team);
        });
        projectRepository.delete(entity);
    }

    @Override
    public void delete(Long id) {
        projectRepository.deleteById(id);
    }

    @Override
    public void deleteInBatch(List<Project> entities) {
        entities.forEach(this::delete);
    }

    @Override
    public Project find(Long id) {
        return projectRepository.getOne(id);
    }

    @Override
    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    public Optional<Project> findByTopic(String topic) {
        List<Project> projects = findAll();
        return projects.stream()
                .filter(t -> t.getTopic().equals(topic))
                .findFirst();
    }
}
