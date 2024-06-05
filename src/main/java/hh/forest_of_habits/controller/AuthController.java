package hh.forest_of_habits.controller;

import hh.forest_of_habits.dto.request.AuthRequest;
import hh.forest_of_habits.dto.request.F2ARequest;
import hh.forest_of_habits.dto.request.RegistrationRequest;
import hh.forest_of_habits.dto.response.AuthResponse;
import hh.forest_of_habits.dto.response.F2AResponse;
import hh.forest_of_habits.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok(authService.login(authRequest));
    }

    @PostMapping("/registration")
    public ResponseEntity<AuthResponse> registration(@RequestBody RegistrationRequest request) {
        return ResponseEntity.ok(authService.registration(request));
    }

    @GetMapping("/f2a")
    public ResponseEntity<F2AResponse> getSecretCode() {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                .build();
    }

    @PostMapping("/f2a")
    public ResponseEntity<AuthResponse> validateF2A(@RequestBody @Valid F2ARequest request) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                .build();
    }
}
