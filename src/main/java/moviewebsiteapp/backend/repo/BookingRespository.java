package moviewebsiteapp.backend.repo;

import moviewebsiteapp.backend.domain.Booking;
import moviewebsiteapp.backend.domain.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRespository extends JpaRepository<Booking, Long> {

    List<Booking> findByEmailAddressIgnoreCaseContaining(String searchTerm);
}
