package pl.edu.agh.pierogi.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.agh.pierogi.model.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
