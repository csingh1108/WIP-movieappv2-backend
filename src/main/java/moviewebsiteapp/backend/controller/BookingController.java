package moviewebsiteapp.backend.controller;

import graphql.GraphQLException;
import moviewebsiteapp.backend.domain.Booking;
import moviewebsiteapp.backend.domain.Movie;
import moviewebsiteapp.backend.domain.Seat;
import moviewebsiteapp.backend.errors.SeatAlreadyTakenException;
import moviewebsiteapp.backend.service.BookingService;
import moviewebsiteapp.backend.service.JwtService;
import moviewebsiteapp.backend.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private SeatService seatService;

    @Autowired
    private JwtService jwtService;

    @QueryMapping
    public Booking getBookingById(@Argument("id") Long id){
        return bookingService.findBookingById(id);
    }

    @QueryMapping
    public List<Booking> getAllBookings(){
        return bookingService.findAllBookings();
    }

    @MutationMapping
    public Booking createBooking(@Argument("movieTimeId") Long movieTimeId,
                                 @Argument("emailAddress") String email,
                                 @Argument("pickedSeats") List<String> pickedSeats,
                                 @Argument("totalPrice") Double totalPrice,
                                 @Argument("userId") Long userId){

        List<Seat> createdSeats = new ArrayList<>();
        try {
            for (String seatName : pickedSeats) {
                Seat createdSeat = seatService.createSeat(seatName, movieTimeId);
                createdSeats.add(createdSeat);
            }
            return bookingService.createBooking(movieTimeId, email, createdSeats, totalPrice, userId);
        }catch(IllegalStateException ex){
            throw new SeatAlreadyTakenException(ex.getMessage());
        }catch (Exception e){
            throw new GraphQLException("There was an error" + e.getMessage());
        }
    }

    @MutationMapping
    public Boolean deleteBooking(@Argument("id") Long bookingId,
                                 @Argument("jwt") String jwt){
        try {
            List<String> authorities = jwtService.extractAuthorities(jwt);
            if (authorities.contains("ROLE_ADMIN")) {
                return bookingService.deleteBooking(bookingId);
            }else{
                throw new IllegalAccessException("Incorrect permissions.");
            }
        } catch (Exception e){
            throw new GraphQLException("Error while deleting booking: " + e.getMessage());
        }
    }

    @QueryMapping
    public List<Booking> searchBookings(@Argument("searchTerm") String searchTerm,
                                    @Argument("jwt") String jwt){
        try {
            List<String> authorities = jwtService.extractAuthorities(jwt);
            if (authorities.contains("ROLE_ADMIN")){
                return bookingService.searchBookings(searchTerm);
            }else {
                throw new IllegalAccessException("Incorrect permissions.");
            }
        }catch(Exception e){
            throw new GraphQLException("There was an error getting the results: " + e.getMessage(), e);
        }
    }
}
