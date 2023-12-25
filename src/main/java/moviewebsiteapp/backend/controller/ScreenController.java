package moviewebsiteapp.backend.controller;

import graphql.GraphQLException;
import io.jsonwebtoken.JwtException;
import moviewebsiteapp.backend.domain.Movie;
import moviewebsiteapp.backend.domain.Screen;
import moviewebsiteapp.backend.service.JwtService;
import moviewebsiteapp.backend.service.ScreenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ScreenController {

    @Autowired
    private ScreenService screenService;

    @Autowired
    private JwtService jwtService;

    @QueryMapping
    public Screen getScreenById(@Argument("id") Long id) {

        try{
            return screenService.getScreenInfo(id);
        }catch(Exception e){
            throw new GraphQLException("There was an error: "+ e.getMessage());
        }
    }

    @QueryMapping
    public List<Screen> getAllScreens() {
        try{
            return screenService.getAllScreenInfo();
        }catch(Exception e){
            throw new GraphQLException("There was an error: "+ e.getMessage());
        }

    }

    @MutationMapping
    public Screen createScreen(@Argument("screenName") String screenName,
                               @Argument("capacity") Integer capacity,
                               @Argument("jwt") String jwt) {
        try {
            if(jwt != null) {
                return screenService.createScreen(screenName, capacity);
            }else{
            throw new IllegalAccessException("Incorrect permissions.");
        }
        } catch (Exception e){
            throw new GraphQLException("Error while creating screen: " + e.getMessage());
        }
    }

    @MutationMapping
    public Screen updateScreen(@Argument("id") Long id,
                               @Argument("screenName") String screenName,
                               @Argument("capacity") Integer capacity,
                               @Argument("jwt") String jwt) {
        try {
            if(jwt != null){
                List<String> authorities = jwtService.extractAuthorities(jwt);
                if (authorities.contains("ROLE_ADMIN")) {
                    return screenService.updateMovieTime(id, screenName, capacity);
                }else{
                    throw new IllegalAccessException("Incorrect permissions.");
                }
            }else{
                throw new JwtException("Jwt token is not valid for this operation.");
            }

        } catch (Exception e){
            throw new GraphQLException("Error while updating screen: " + e.getMessage());
        }

    }

    @MutationMapping
    public Boolean deleteScreen(@Argument("id") Long id,
                                @Argument("jwt") String jwt){
        try {
            if(jwt != null) {
                List<String> authorities = jwtService.extractAuthorities(jwt);
                if (authorities.contains("ROLE_ADMIN")) {
                    return screenService.deleteScreen(id);
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
    public List<Screen> searchScreens(@Argument("searchTerm") String searchTerm,
                                     @Argument("jwt") String jwt){

        try {
            List<String> authorities = jwtService.extractAuthorities(jwt);
            if (authorities.contains("ROLE_ADMIN")){
                return screenService.searchScreens(searchTerm);
            }else {
                throw new IllegalAccessException("Incorrect permissions.");
            }
        }catch(Exception e){
            throw new GraphQLException("There was an error getting the results: " + e.getMessage(), e);
        }
    }

}
