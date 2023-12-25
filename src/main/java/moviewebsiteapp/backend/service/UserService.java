package moviewebsiteapp.backend.service;

import lombok.RequiredArgsConstructor;
import moviewebsiteapp.backend.domain.Movie;
import moviewebsiteapp.backend.domain.User;
import moviewebsiteapp.backend.repo.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDetailsService userDetailsService(){
        return email -> userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("Username not found"));
    }

    public User register(User newUser){
        if (newUser.getId() == null) {
            newUser.setCreatedAt(LocalDateTime.now());
        }
        newUser.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(newUser);
    }

    public User findUserById(Long id) {
        return userRepository.findById(id).orElse(new User());
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public boolean deleteUser(Long id) {
        try{
            userRepository.deleteById(id);
            return true;
        } catch(Exception e){
            throw new NoSuchElementException("User not found");
        }
    }

    public List<User> searchUsers(String searchTerm) {
        return userRepository.findByFirstNameIgnoreCaseContainingOrLastNameIgnoreCaseContaining(searchTerm, searchTerm);
    }

}
