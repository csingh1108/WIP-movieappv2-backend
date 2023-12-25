package moviewebsiteapp.backend.service;

import moviewebsiteapp.backend.domain.Movie;
import moviewebsiteapp.backend.domain.Screen;
import moviewebsiteapp.backend.repo.ScreenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ScreenService {

    @Autowired
    private ScreenRepository screenRepository;

    public Screen getScreenInfo(Long id) {
        Optional<Screen> screenOpt = screenRepository.findById(id);
        if(screenOpt.isPresent()){
            return screenOpt.get();
        } else {
            throw new NoSuchElementException();
        }
    }

    public List<Screen> getAllScreenInfo() {
        return screenRepository.findAll();
    }

    public Screen createScreen(String screenName, Integer capacity) {
        Screen newScreen = new Screen();
        newScreen.setScreenName(screenName);
        newScreen.setCapacity(capacity);
        newScreen.setCreatedDate(LocalDateTime.now());
        newScreen.setUpdatedDate(LocalDateTime.now());
        return screenRepository.save(newScreen);
    }

    public Screen updateMovieTime(Long id, String screenName, Integer capacity) {
        Optional<Screen> screenToUpdateOpt = screenRepository.findById(id);
        if(screenToUpdateOpt.isPresent()){
            Screen screenToUpdate = screenToUpdateOpt.get();
            screenToUpdate.setScreenName(screenName);
            screenToUpdate.setCapacity(capacity);
            screenToUpdate.setUpdatedDate(LocalDateTime.now());
            return screenRepository.save(screenToUpdate);
        } else {
            throw new NoSuchElementException();
        }
    }

    public Boolean deleteScreen(Long id) {
        if(screenRepository.existsById(id)){
            screenRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public List<Screen> searchScreens(String searchTerm) {
        return screenRepository.findByscreenNameIgnoreCaseContaining(searchTerm);
    }
}