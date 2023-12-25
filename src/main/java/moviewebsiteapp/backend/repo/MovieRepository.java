package moviewebsiteapp.backend.repo;

import moviewebsiteapp.backend.domain.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findByTitleIgnoreCaseContaining(String searchTerm);

    @Query("SELECT m FROM Movie m LEFT JOIN FETCH m.movieTimes WHERE m.id = :id")
    Movie findByIdWithMovieTimes(@Param("id") Long id);
}

