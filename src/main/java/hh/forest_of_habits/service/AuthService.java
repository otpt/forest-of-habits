package hh.forest_of_habits.service;

import hh.forest_of_habits.dto.request.AuthRequest;
import hh.forest_of_habits.dto.request.RegistrationRequest;
import hh.forest_of_habits.dto.response.AuthResponse;
import hh.forest_of_habits.exception.AuthenticationException;
import hh.forest_of_habits.exception.EmailAlreadyExistsException;
import hh.forest_of_habits.exception.UserAlreadyExistsException;
import hh.forest_of_habits.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
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

    public AuthResponse login(@RequestBody AuthRequest authRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()
                    ));
        } catch (BadCredentialsException exception) {
            throw new AuthenticationException();
        }
        return authResponse((UserDetails) authentication.getPrincipal());
    }

    public AuthResponse registration(@RequestBody RegistrationRequest registrationRequest) {
        String username = registrationRequest.getUsername();
        String password = registrationRequest.getPassword();
        String email = registrationRequest.getEmail();

        if (userService.findByName(username).isPresent())
            throw new UserAlreadyExistsException(username);

        if (userService.findByEmail(email).isPresent())
            throw new EmailAlreadyExistsException(email);

        userService.createNewUser(registrationRequest);

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                username,
                password,
                List.of()
        );
        return authResponse(userDetails);
    }

    private AuthResponse authResponse(UserDetails userDetails) {
        String token = tokenUtils.generateToken(userDetails);
        return AuthResponse.builder()
                .token(token)
                .userName(userDetails.getUsername())
                .build();
    }
}
