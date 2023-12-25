package moviewebsiteapp.backend.service;

import jakarta.transaction.Transactional;
import moviewebsiteapp.backend.domain.MovieTime;
import moviewebsiteapp.backend.domain.Seat;
import moviewebsiteapp.backend.repo.MovieTimeRepository;
import moviewebsiteapp.backend.repo.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SeatService {
    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private MovieTimeRepository movieTimeRepository;

    public Seat findSeatById(Long id) {
        // Retrieves a seat by its id. Returns null if the seat is not found.
        Optional<Seat> seat = seatRepository.findById(id);
        return seat.orElse(null);
    }

    public List<Seat> findAllSeats() {
        // Retrieves all seats as a list.
        return seatRepository.findAll();
    }

    @Transactional
    public Seat createSeat(String seatName, Long movieTimeId) {
        // Creates a new seat with the provided details and saves it.
        if (seatName == null || movieTimeId == null) {
            throw new IllegalArgumentException("All seat info was not filled");
        } else {
            Optional<MovieTime> movieTimeOptional = movieTimeRepository.findById(movieTimeId);
            if (movieTimeOptional.isPresent()) {
                MovieTime movieTime = movieTimeOptional.get();

                Optional<Seat> existingSeat = movieTime.getReservedSeats().stream()
                        .filter(seat -> seat.getSeatName() != null && seat.getSeatName().equals(seatName))
                        .findFirst();

                if (existingSeat.isPresent()) {
                    Seat seat = existingSeat.get();
                    if (seat.getIsTaken()) {
                        throw new IllegalStateException("Seat "+ seatName +" is already taken for this Movie time.");
                    } else {
                        seat.setIsTaken(true);
                        movieTimeRepository.save(movieTime);
                        return seatRepository.save(seat);
                    }
                } else {
                    Seat newSeat = new Seat();
                    newSeat.setSeatName(seatName);
                    newSeat.setIsTaken(true);
                    newSeat.setMovieTime(movieTime);

                    movieTime.getReservedSeats().add(newSeat);
                    movieTimeRepository.save(movieTime);

                    return seatRepository.save(newSeat);
                }
            } else {
                throw new IllegalArgumentException("MovieTime with ID " + movieTimeId + " not found");
            }
        }
    }

    public Seat updateSeat(Long id, String seatName, Boolean isTaken, Long movieTimeId) {
        Optional<Seat> optionalSeat = seatRepository.findById(id);
        if (optionalSeat.isPresent()) {
            Seat seat = optionalSeat.get();
            if (seatName != null) {
                seat.setSeatName(seatName);
            }
            if (isTaken != null) {
                seat.setIsTaken(isTaken);
            }
            if (movieTimeId != null) {
                Optional<MovieTime> movieTimeOptional = movieTimeRepository.findById(movieTimeId);
                movieTimeOptional.ifPresent(seat::setMovieTime);
            }
            return seatRepository.save(seat);
        } else {
            return null;
        }
    }
    public void deleteSeat(Long id) {
        seatRepository.deleteById(id);
    }
}