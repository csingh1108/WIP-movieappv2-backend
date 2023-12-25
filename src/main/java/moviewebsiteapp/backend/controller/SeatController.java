package moviewebsiteapp.backend.controller;

import graphql.GraphQLException;
import moviewebsiteapp.backend.domain.Seat;
import moviewebsiteapp.backend.errors.SeatAlreadyTakenException;
import moviewebsiteapp.backend.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SeatController {

    @Autowired
    private SeatService seatService;

    @QueryMapping
    public Seat getSeatById(@Argument("id") Long id){
        return seatService.findSeatById(id);
    }

    @QueryMapping
    public List<Seat> getAllSeats(){
        return seatService.findAllSeats();
    }

    @MutationMapping
    public List<Seat> createSeats(@Argument("seatNames") List<String> seatNames,
                                  @Argument("movieTimeId") Long movieTimeId) {

        List<Seat> createdSeats = new ArrayList<>();

        try {
            for (String seatName : seatNames) {
                Seat createdSeat = seatService.createSeat(seatName, movieTimeId);
                createdSeats.add(createdSeat);
            }
            return createdSeats;
        } catch (Exception e) {
            throw new SeatAlreadyTakenException(e.getMessage());
        }
    }

    @MutationMapping
    public Seat updateSeat(@Argument("id") Long id,
                           @Argument("seatName") String seatName,
                           @Argument("isTaken") Boolean isTaken,
                           @Argument("movieTimeId") Long movieTimeId) {
        try{
            return seatService.updateSeat(id, seatName, isTaken, movieTimeId);
        } catch (Exception e){
            throw new GraphQLException("There was an error updating the seat: " + e.getMessage());
        }
    }

    @MutationMapping
    public boolean deleteSeat(@Argument("id") Long id){
        try{
            seatService.deleteSeat(id);
            return true;
        } catch (Exception e){
            throw new GraphQLException("There was an error deleting the seat: " + e.getMessage());
        }
    }
}