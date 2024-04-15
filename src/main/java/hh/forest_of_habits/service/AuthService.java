package hh.forest_of_habits.service;

import hh.forest_of_habits.dto.request.AuthRequest;
import hh.forest_of_habits.dto.response.AuthResponse;
import hh.forest_of_habits.dto.request.RegistrationRequest;
import hh.forest_of_habits.dto.response.ErrorResponse;
import hh.forest_of_habits.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtTokenUtils tokenUtils;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authRequest.getUsername(),
                    authRequest.getPassword()
            ));
        } catch (BadCredentialsException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorResponse.builder()
                            .message("Неверный логин или пароль")
                            .build());
        }
        return ResponseEntity.ok(
                authResponse((UserDetails) authentication.getPrincipal()));
    }

    public ResponseEntity<?> registration(@RequestBody RegistrationRequest registrationRequest) {

        if (userService.findByName(registrationRequest.getUsername()).isPresent()) {
            return ResponseEntity.badRequest()
                    .body(ErrorResponse.builder()
                            .message("Пользователь уже существует")
                            .build());
        }

        userService.createNewUser(registrationRequest);

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                registrationRequest.getUsername(),
                registrationRequest.getPassword(),
                List.of()
        );
        return ResponseEntity.ok(authResponse(userDetails));
    }

    private AuthResponse authResponse(UserDetails userDetails) {
        String token = tokenUtils.generateToken(userDetails);
        return AuthResponse.builder()
                .token(token)
                .userName(userDetails.getUsername())
                .build();
    }
}
