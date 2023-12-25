package moviewebsiteapp.backend.controller;

import graphql.GraphQLException;
import io.jsonwebtoken.JwtException;
import moviewebsiteapp.backend.domain.User;
import moviewebsiteapp.backend.service.JwtService;
import moviewebsiteapp.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;


@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @QueryMapping
    public User getUserById(@Argument("id") Long id){
        return userService.findUserById(id);
    }

    @QueryMapping
    public List<User> getAllUsers(@Argument("jwt") String jwt) {
        try {
            if (jwt != null) {
                List<String> authorities = jwtService.extractAuthorities(jwt);
                if (authorities.contains("ROLE_ADMIN")) {
                    return userService.findAllUsers();
                } else {
                    throw new IllegalAccessException("Incorrect permissions.");
                }
            } else {
                throw new JwtException("Jwt token is not valid for this operation.");
            }
        } catch (Exception e) {
            throw new GraphQLException("Error while deleting movie: " + e.getMessage());
        }
    }

    @MutationMapping
    public boolean deleteUser(@Argument("id") Long id,
                              @Argument("jwt") String jwt){
        try {
            if(jwt != null) {
                List<String> authorities = jwtService.extractAuthorities(jwt);
                if (authorities.contains("ROLE_ADMIN")) {
                    return userService.deleteUser(id);
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
    public List<User> searchUsers(@Argument("searchTerm") String searchTerm,
                                  @Argument("jwt") String jwt){

        try {
            List<String> authorities = jwtService.extractAuthorities(jwt);
            if (authorities.contains("ROLE_ADMIN")){
                return userService.searchUsers(searchTerm);
            }else {
                throw new IllegalAccessException("Incorrect permissions.");
            }
        }catch(Exception e){
            throw new GraphQLException("There was an error getting the results: " + e.getMessage(), e);
        }
    }

}
