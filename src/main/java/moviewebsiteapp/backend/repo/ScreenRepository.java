package moviewebsiteapp.backend.repo;

import moviewebsiteapp.backend.domain.Movie;
import moviewebsiteapp.backend.domain.Screen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScreenRepository extends JpaRepository<Screen, Long> {
    List<Screen> findByscreenNameIgnoreCaseContaining(String searchTerm);
}
