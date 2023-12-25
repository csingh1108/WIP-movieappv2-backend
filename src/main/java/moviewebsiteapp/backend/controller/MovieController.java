package moviewebsiteapp.backend.controller;

import graphql.GraphQLException;

import io.jsonwebtoken.JwtException;
import moviewebsiteapp.backend.domain.Movie;
import moviewebsiteapp.backend.service.JwtService;
import moviewebsiteapp.backend.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class MovieController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private JwtService jwtService;

    @QueryMapping
    public Movie getMovieById(@Argument("id") Long id) {
        return movieService.getMovie(id);
    }

    @QueryMapping
    public List<Movie> getAllMovies() {
        return movieService.getAllMovies();
    }

    @MutationMapping
    public Movie createMovie(@Argument("title") String title,
                             @Argument("synopsis") String synopsis,
                             @Argument("releaseYear") Integer releaseYear,
                             @Argument("duration") Integer duration,
                             @Argument("imgUrl") String imgUrl,
                             @Argument("videoUrl") String videoUrl,
                             @Argument("rating") String rating,
                             @Argument("genre") String genre,
                             @Argument("isFeatured") Boolean isFeatured,
                             @Argument("jwt") String jwt) {
        try {
            List<String> authorities = jwtService.extractAuthorities(jwt);
            if (authorities.contains("ROLE_ADMIN")) {
                return movieService.createMovie(title, synopsis, releaseYear, duration, imgUrl, videoUrl, rating, genre, isFeatured);
            }else{
                throw new IllegalAccessException("Incorrect permissions.");
            }
        } catch (Exception e){
            throw new GraphQLException("Error while creating movie: " + e.getMessage());
        }
    }

    @MutationMapping
    public Movie updateMovie(@Argument("id") Long id,
                            @Argument("title") String title,
                             @Argument("synopsis") String synopsis,
                             @Argument("releaseYear") Integer releaseYear,
                             @Argument("duration") Integer duration,
                             @Argument("imgUrl") String imgUrl,
                             @Argument("videoUrl") String videoUrl,
                             @Argument("rating") String rating,
                             @Argument("genre") String genre,
                             @Argument("isFeatured") Boolean isFeatured,
                             @Argument("jwt") String jwt) {
        try {
            if(jwt != null){
                List<String> authorities = jwtService.extractAuthorities(jwt);
                if (authorities.contains("ROLE_ADMIN")) {
                    return movieService.updateMovie(id, title, synopsis, releaseYear, duration, imgUrl, videoUrl, rating, genre, isFeatured);
                }else{
                    throw new IllegalAccessException("Incorrect permissions.");
                }
            }else{
                throw new JwtException("Jwt token is not valid for this operation.");
            }

        } catch (Exception e){
            throw new GraphQLException("Error while updating movie: " + e.getMessage());
        }

    }

    @MutationMapping
    public Boolean deleteMovie(@Argument("id") Long id, @Argument("jwt") String jwt){
        try {
            if(jwt != null) {
                List<String> authorities = jwtService.extractAuthorities(jwt);
                if (authorities.contains("ROLE_ADMIN")) {
                    return movieService.deleteMovie(id);
                } else {
                    throw new IllegalAccessException("Incorrect permissions.");
                }
            }else{
                throw new JwtException("Jwt token is not valid for this operation.");
            }
        } catch (Exception e){
            throw new GraphQLException("Error while deleting movie: " + e.getMessage());
        }
    }

    @QueryMapping
    public List<Movie> searchMovies(@Argument("searchTerm") String searchTerm,
                                    @Argument("jwt") String jwt){

        try {
            List<String> authorities = jwtService.extractAuthorities(jwt);
            if (authorities.contains("ROLE_ADMIN")){
                return movieService.searchMovies(searchTerm);
            }else {
                throw new IllegalAccessException("Incorrect permissions.");
            }
        }catch(Exception e){
            throw new GraphQLException("There was an error getting the results: " + e.getMessage(), e);
        }
    }

}
