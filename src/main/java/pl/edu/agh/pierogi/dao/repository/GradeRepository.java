package pl.edu.agh.pierogi.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.agh.pierogi.model.Grade;

public interface GradeRepository extends JpaRepository<Grade, Long> {
}
