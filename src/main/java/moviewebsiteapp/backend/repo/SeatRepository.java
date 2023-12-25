package moviewebsiteapp.backend.repo;

import moviewebsiteapp.backend.domain.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Long> {

}
