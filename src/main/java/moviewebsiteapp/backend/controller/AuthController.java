package moviewebsiteapp.backend.controller;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import moviewebsiteapp.backend.DTO.JwtAuthenticationResponse;
import moviewebsiteapp.backend.DTO.SignInRequest;
import moviewebsiteapp.backend.DTO.SignUpRequest;
import moviewebsiteapp.backend.domain.User;
import moviewebsiteapp.backend.service.AuthService;
import moviewebsiteapp.backend.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authenticationService;

    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody SignUpRequest request) {
        String token = String.valueOf(authenticationService.register(request));
        if (!token.isEmpty()) {
            return ResponseEntity.ok("Successfully Registered");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There was an error processing the request");
        }
    }

    @PostMapping("/login")
    public JwtAuthenticationResponse login(@RequestBody SignInRequest request) {
        return authenticationService.login(request);
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestParam String token, @AuthenticationPrincipal User user){
        try{
            System.out.println(user.getEmail());
            Boolean isTokenValid= jwtService.isTokenValid(token, user);
            return ResponseEntity.ok(isTokenValid);
        }catch (ExpiredJwtException e){
            return ResponseEntity.ok(false);
        }
    }

}
