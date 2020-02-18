package pl.edu.agh.pierogi.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.agh.pierogi.model.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
