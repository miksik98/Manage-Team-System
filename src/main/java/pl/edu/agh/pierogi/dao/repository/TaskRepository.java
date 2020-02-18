package pl.edu.agh.pierogi.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.agh.pierogi.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
