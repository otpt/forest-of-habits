package hh.forest_of_habits.controller;

import hh.forest_of_habits.dto.AuthRequest;
import hh.forest_of_habits.dto.AuthResponse;
import hh.forest_of_habits.dto.RegistrationRequest;
import hh.forest_of_habits.exception.AppException;
import hh.forest_of_habits.service.UserService;
import hh.forest_of_habits.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtTokenUtils tokenUtils;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authRequest.getUsername(),
                    authRequest.getPassword()
            ));
        } catch (BadCredentialsException exception) {
            return new ResponseEntity<>(
                    new AppException(
                            HttpStatus.UNAUTHORIZED.value(),
                            "Неверный логин или пароль"
                    ),
                    HttpStatus.UNAUTHORIZED);
        }

        UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
        return ResponseEntity.ok(new AuthResponse(tokenUtils.generateToken(userDetails)));
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody RegistrationRequest request) {
        if (userService.findByName(request.getUsername()).isPresent()) {
            return new ResponseEntity<>(new AppException(
                    HttpStatus.BAD_REQUEST.value(),
                    "Пользователь уже существует"),
                    HttpStatus.BAD_REQUEST
            );
        }

        userService.createNewUser(request);
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                request.getUsername(),
                request.getPassword(),
                List.of()
        );

        String token = tokenUtils.generateToken(userDetails);
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
