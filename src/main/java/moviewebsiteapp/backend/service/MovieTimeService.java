package moviewebsiteapp.backend.service;

import moviewebsiteapp.backend.domain.Movie;
import moviewebsiteapp.backend.domain.MovieTime;

import moviewebsiteapp.backend.domain.Screen;
import moviewebsiteapp.backend.domain.Seat;
import moviewebsiteapp.backend.repo.MovieRepository;
import moviewebsiteapp.backend.repo.MovieTimeRepository;
import moviewebsiteapp.backend.repo.ScreenRepository;
import moviewebsiteapp.backend.repo.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MovieTimeService {

    @Autowired
    private MovieTimeRepository movieTimeRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ScreenRepository screenRepository;

    @Autowired
    private SeatRepository seatRepository;

    public MovieTime getMovieTime(Long id) {
        return movieTimeRepository.findById(id).orElse(null);
    }

    public List<MovieTime> getAllMovieTimes() {
        return movieTimeRepository.findAll();
    }

    public MovieTime createMovieTime(String startTime, Float adultPrice, Float seniorPrice, Float childPrice, Boolean isFull,
                                     Long movieId, Long screenId) {
        if (startTime == null || adultPrice == null || seniorPrice == null || childPrice == null || isFull == null
                || movieId == null || screenId == null) {
            throw new IllegalArgumentException("All fields must be defined for the movie");
        }

        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new RuntimeException("Movie not found"));
        Screen screen = screenRepository.findById(screenId).orElseThrow(() -> new RuntimeException("Screen not found"));

        MovieTime movieTime = new MovieTime();
        movieTime.setStartTime(LocalDateTime.parse(startTime));
        movieTime.setAdultPrice(adultPrice);
        movieTime.setSeniorPrice(seniorPrice);
        movieTime.setChildPrice(childPrice);
        movieTime.setIsFull(isFull);
        movieTime.setMovie(movie);
        movieTime.setScreen(screen);
        movieTime.setCreatedDate(LocalDateTime.now());
        movieTime.setUpdatedDate(LocalDateTime.now());

        return movieTimeRepository.save(movieTime);
    }

    public MovieTime updateMovieTime(Long id, String startTime, Float adultPrice, Float seniorPrice, Float childPrice,
                                     Boolean isFull) {
        MovieTime movieTime = movieTimeRepository.findById(id).orElseThrow(() -> new RuntimeException("MovieTime not found"));

        if (startTime != null) {
            movieTime.setStartTime(LocalDateTime.parse(startTime));
        }
        if (adultPrice != null) {
            movieTime.setAdultPrice(adultPrice);
        }
        if (seniorPrice != null) {
            movieTime.setSeniorPrice(seniorPrice);
        }
        if (childPrice != null) {
            movieTime.setChildPrice(childPrice);
        }
        if (isFull != null) {
            movieTime.setIsFull(isFull);
        }
        movieTime.setUpdatedDate(LocalDateTime.now());

        return movieTimeRepository.save(movieTime);
    }

    public Boolean deleteMovieTime(Long id) {
        try {
            // Fetch the MovieTime entity by id
            Optional<MovieTime> optionalMovieTime = movieTimeRepository.findById(id);

            if (optionalMovieTime.isPresent()) {
                MovieTime movieTime = optionalMovieTime.get();

                List<Seat> reservedSeats = movieTime.getReservedSeats();
                seatRepository.deleteAll(reservedSeats);


                movieTimeRepository.deleteById(id);

                return true;
            } else {

                return false;
            }
        } catch (Exception e) {

            return false;
        }
    }


}