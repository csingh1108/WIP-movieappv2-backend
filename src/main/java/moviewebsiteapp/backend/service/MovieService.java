package moviewebsiteapp.backend.service;

import moviewebsiteapp.backend.domain.Movie;
import moviewebsiteapp.backend.domain.MovieTime;
import moviewebsiteapp.backend.repo.MovieRepository;
import moviewebsiteapp.backend.repo.MovieTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieTimeRepository movieTimeRepository;
    public Movie createMovie(String title, String synopsis, Integer releaseYear,
                             Integer duration, String imgUrl, String videoUrl, String rating, String genre, Boolean isFeatured) {

        if (title != null && synopsis != null && releaseYear != null && duration != null
                && imgUrl != null && videoUrl != null && rating != null && genre != null && isFeatured != null) {

            Movie newMovie = new Movie();
            newMovie.setTitle(title);
            newMovie.setSynopsis(synopsis);
            newMovie.setReleaseYear(releaseYear);
            newMovie.setDuration(duration);
            newMovie.setImgUrl(imgUrl);
            newMovie.setVideoUrl(videoUrl);
            newMovie.setRating(rating);
            newMovie.setGenre(genre);
            newMovie.setIsFeatured(isFeatured);
            newMovie.setCreatedDate(LocalDateTime.now());
            newMovie.setUpdatedDate(LocalDateTime.now());

            return movieRepository.save(newMovie);
        } else {
            throw new IllegalArgumentException("All fields must be defined for the movie");
        }
    }


    public Movie updateMovie(Long id, String title, String synopsis, Integer releaseYear, Integer duration, String imgUrl,
                             String videoUrl, String rating, String genre, Boolean isFeatured) {
        Movie movieToUpdate = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie with id " + id + " not found"));

        if (title != null) movieToUpdate.setTitle(title);
        if (synopsis != null) movieToUpdate.setSynopsis(synopsis);
        if (releaseYear != null) movieToUpdate.setReleaseYear(releaseYear);
        if (duration != null) movieToUpdate.setDuration(duration);
        if (imgUrl != null) movieToUpdate.setImgUrl(imgUrl);
        if (videoUrl != null) movieToUpdate.setVideoUrl(videoUrl);
        if (rating != null) movieToUpdate.setRating(rating);
        if (genre != null) movieToUpdate.setGenre(genre);
        if (isFeatured != null) movieToUpdate.setIsFeatured(isFeatured);
        movieToUpdate.setUpdatedDate(LocalDateTime.now());

        return movieRepository.save(movieToUpdate);
    }

    public boolean deleteMovie(Long id) {
        if (movieRepository.existsById(id)) {
            // Fetch the movie along with its associated movie times
            Movie movie = movieRepository.findByIdWithMovieTimes(id);

            // Delete associated movie times
            for (MovieTime movieTime : movie.getMovieTimes()) {
                deleteMovieTime(movieTime.getId());
            }

            // Now, delete the movie itself
            movieRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    private void deleteMovieTime(Long id) {
        movieTimeRepository.deleteById(id);
    }



    public Movie getMovie(Long id) {
        Optional<Movie> movieOpt = movieRepository.findById(id);
        if (movieOpt.isPresent()) {
            return movieOpt.get();
        }else{
            throw new NoSuchElementException("Movie was not found");
        }
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public List<Movie> searchMovies(String searchTerm) {
        return movieRepository.findByTitleIgnoreCaseContaining(searchTerm);

    }
}
