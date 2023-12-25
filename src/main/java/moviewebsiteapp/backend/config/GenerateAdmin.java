package moviewebsiteapp.backend.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moviewebsiteapp.backend.domain.User;
import moviewebsiteapp.backend.enums.Role;
import moviewebsiteapp.backend.repo.UserRepository;
import moviewebsiteapp.backend.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j
public class GenerateAdmin implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Override
    public void run(String... args) throws Exception {

        if (userRepository.count() == 0) {

            User admin = User
                    .builder()
                    .firstName("admin")
                    .lastName("admin")
                    .email("admin@admin.com")
                    .phone("1233123123")
                    .dateOfBirth(LocalDate.of(1990, 5, 15))
                    .address("1234 Sesame Street")
                    .password(passwordEncoder.encode("password"))
                    .role(Role.ROLE_ADMIN)
                    .build();

            userService.register(admin);
        }
    }
}
