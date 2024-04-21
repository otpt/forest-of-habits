package hh.forest_of_habits.service;

import hh.forest_of_habits.dto.request.RegistrationRequest;
import hh.forest_of_habits.entity.User;
import hh.forest_of_habits.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private static final String USER_NOT_FOUND = "User '%s' not found";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<User> findByName(String name) {
        return userRepository.findByName(name);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByName(username).orElseThrow(() -> new UsernameNotFoundException(
                USER_NOT_FOUND.formatted(username)
        ));

        return new org.springframework.security.core.userdetails.User(
                user.getName(),
                user.getPassword(),
                List.of()
        );
    }

    public void createNewUser(RegistrationRequest request) {
        User user = User.builder()
                .name(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .agreementConfirmation(request.getAgreementConfirmation())
                .build();
        userRepository.save(user);
    }
}
