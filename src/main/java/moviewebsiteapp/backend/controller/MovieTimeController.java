package moviewebsiteapp.backend.controller;

import graphql.GraphQLException;

import moviewebsiteapp.backend.domain.MovieTime;
import moviewebsiteapp.backend.service.JwtService;
import moviewebsiteapp.backend.service.MovieTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class MovieTimeController {

    @Autowired
    private MovieTimeService movieTimeService;

    @Autowired
    private JwtService jwtService;

    @QueryMapping
    public MovieTime getMovieTimeById(@Argument("id") Long id) {
        return movieTimeService.getMovieTime(id);
    }

    @QueryMapping
    public List<MovieTime> getAllMovieTimes() {
        return movieTimeService.getAllMovieTimes();
    }

    @MutationMapping
    public MovieTime createMovieTime(@Argument("startTime") String startTime,
                                     @Argument("adultPrice") Float adultPrice,
                                     @Argument("seniorPrice") Float seniorPrice,
                                     @Argument("childPrice") Float childPrice,
                                     @Argument("isFull") Boolean isFull,
                                     @Argument("movieId") Long movieId,
                                     @Argument("screenId") Long screenId) {
        try {
            return movieTimeService.createMovieTime(startTime, adultPrice, seniorPrice, childPrice, isFull, movieId, screenId);
        } catch (Exception e){
            throw new GraphQLException("Error while creating movie time: " + e.getMessage());
        }
    }

    @MutationMapping
    public MovieTime updateMovieTime(@Argument("id") Long id,
                                     @Argument("startTime") String startTime,
                                     @Argument("adultPrice") Float adultPrice,
                                     @Argument("seniorPrice") Float seniorPrice,
                                     @Argument("childPrice") Float childPrice,
                                     @Argument("isFull") Boolean isFull) {
        return movieTimeService.updateMovieTime(id, startTime, adultPrice, seniorPrice, childPrice, isFull);
    }

    @MutationMapping
    public Boolean deleteMovieTime(@Argument("id") Long id,
                                   @Argument("jwt") String jwt){
        try {
            List<String> authorities = jwtService.extractAuthorities(jwt);
            if (authorities.contains("ROLE_ADMIN")){
                return movieTimeService.deleteMovieTime(id);
            }else {
                throw new IllegalAccessException("Incorrect permissions.");
            }
        }catch(Exception e){
            throw new GraphQLException("There was an error deleting the movie time: " + e.getMessage(), e);
        }
    }

}