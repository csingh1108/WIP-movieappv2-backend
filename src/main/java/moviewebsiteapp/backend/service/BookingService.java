package moviewebsiteapp.backend.service;

import moviewebsiteapp.backend.domain.Booking;
import moviewebsiteapp.backend.domain.Movie;
import moviewebsiteapp.backend.domain.MovieTime;
import moviewebsiteapp.backend.domain.Seat;
import moviewebsiteapp.backend.repo.BookingRespository;
import moviewebsiteapp.backend.repo.MovieTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRespository bookingRespository;

    @Autowired
    private MovieTimeRepository movieTimeRepository;

    public Booking findBookingById(Long id) {
        Optional<Booking> bookingOptional = bookingRespository.findById(id);
        return bookingOptional.orElse(null);
    }

    public List<Booking> findAllBookings() {
        return bookingRespository.findAll();
    }

    public Booking createBooking(Long movieTimeId, String email, List<Seat> createdSeats, Double totalPrice, Long userId) {
        if(movieTimeId != null && email != null && createdSeats != null && totalPrice != null){
            Booking booking = new Booking();
            Optional<MovieTime> movieTimeOptional = movieTimeRepository.findById(movieTimeId);
            movieTimeOptional.ifPresent(booking::setMovieTime);
            booking.setEmailAddress(email);
            if(userId != null){
                booking.setUserId(userId);
            }
            booking.setPickedSeats(createdSeats);
            booking.setTotalPrice(totalPrice);
            booking.setCreatedDate(LocalDateTime.now());
            booking.setUpdatedDate(LocalDateTime.now());
            return bookingRespository.save(booking);
        } else{
            throw new IllegalArgumentException("Booking data was not complete.");
        }

    }

    public Boolean deleteBooking(Long bookingId) {
        if(bookingRespository.existsById(bookingId)) {
            bookingRespository.deleteById(bookingId);
            return true;
        } else {
            return false;
        }
    }

    public List<Booking> searchBookings(String searchTerm) {
        return bookingRespository.findByEmailAddressIgnoreCaseContaining(searchTerm);
    }
}
