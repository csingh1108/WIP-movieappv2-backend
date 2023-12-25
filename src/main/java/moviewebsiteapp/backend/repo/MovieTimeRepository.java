package moviewebsiteapp.backend.repo;

import moviewebsiteapp.backend.domain.MovieTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieTimeRepository extends JpaRepository<MovieTime, Long> {
}
