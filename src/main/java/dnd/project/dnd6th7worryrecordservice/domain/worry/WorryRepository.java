package dnd.project.dnd6th7worryrecordservice.domain.worry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorryRepository extends JpaRepository<Worry, Long> {

}
