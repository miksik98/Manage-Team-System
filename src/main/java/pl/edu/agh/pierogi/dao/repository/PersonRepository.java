package pl.edu.agh.pierogi.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.agh.pierogi.model.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
