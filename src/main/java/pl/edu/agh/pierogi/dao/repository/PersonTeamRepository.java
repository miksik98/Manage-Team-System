package pl.edu.agh.pierogi.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.agh.pierogi.model.PersonTeam;

public interface PersonTeamRepository extends JpaRepository<PersonTeam, Long> {
}
