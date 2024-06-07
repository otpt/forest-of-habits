package hh.forest_of_habits;

import hh.forest_of_habits.dto.request.AuthRequest;
import hh.forest_of_habits.dto.request.RegistrationRequest;
import hh.forest_of_habits.entity.User;
import hh.forest_of_habits.repository.UserRepository;
import hh.forest_of_habits.service.AuthService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.net.URI;
import java.util.stream.Stream;

public class AuthenticationTests extends ForestOfHabitsApplicationTests {

    private static final String URI_LOGIN = "/auth/login";
    private static final String URI_REGISTRATION = "/auth/registration";

    @BeforeEach
    void setUp(@Autowired AuthService authService) {
        authService.registration(RegistrationRequest.builder()
                        .username(testUser.getName())
                        .password(testUser.getPassword())
                        .email(testUser.getEmail())
                        .agreementConfirmation(testUser.getAgreementConfirmation())
                .build());
    }

    @AfterEach
    void tearDown(@Autowired UserRepository userRepository) {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Корректная регистрация")
    void registrationTest() {
        String username = "username";
        var request = RegistrationRequest.builder()
                .username(username)
                .password("password")
                .email("email@gmail.com")
                .agreementConfirmation(true)
                .build();

        webClient.post()
                .uri(URI_REGISTRATION)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.token").isNotEmpty()
                .jsonPath("$.userName").isEqualTo(username);
    }

    @ParameterizedTest
    @CsvSource({
            ", pass, email, true",
            "user, , email, true",
            "user, pass, , true",
            "user, pass, email, ",
            "alex123, wqelkhey1298@13SA, myRealEmail@gmail.com, false"
    })
    @DisplayName("Невалидные данные при регистрации")
    void registrationInvalidBodyTest(String username, String password, String email, Boolean agreement) {
        var request = RegistrationRequest.builder()
                .username(username)
                .password(password)
                .email(email)
                .agreementConfirmation(agreement)
                .build();

        HttpStatus expectedStatus = Boolean.FALSE.equals(agreement) ?
                HttpStatus.CONFLICT :
                HttpStatus.BAD_REQUEST;

        webClient.post()
                .uri(URI_REGISTRATION)
                .bodyValue(request)
                .exchange()
                .expectStatus().isEqualTo(expectedStatus)
                .expectBody()
                .jsonPath("$.message").isNotEmpty()
                .jsonPath("$.timestamp").isNotEmpty();
    }


    static Stream<? extends Arguments> registrationWithExistDataTest() {
        return Stream.of(
                Arguments.of(testUser.getName(), "anotherEmail@gmail.com"),
                Arguments.of("anotherUsername", testUser.getEmail())
        );
    }

    @ParameterizedTest
    @MethodSource
    @DisplayName("Регистрация с уже существующими username или email")
    void registrationWithExistDataTest(String username, String email) {
        var request = RegistrationRequest.builder()
                .username(username)
                .password("password")
                .email(email)
                .agreementConfirmation(true)
                .build();

        webClient.post()
                .uri(URI_REGISTRATION)
                .bodyValue(request)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void successLogin() {
        var request = AuthRequest.builder()
                .username(testUser.getName())
                .password(testUser.getPassword())
                .build();

        webClient.post()
                .uri(URI_LOGIN)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.token").isNotEmpty()
                .jsonPath("$.userName").isNotEmpty();
    }

    @Test
    void badRequestLogin() {
        String username = "";
        String password = "";

        var request = AuthRequest.builder()
                .username(username)
                .password(password)
                .build();

        webClient.post()
                .uri(URI_LOGIN)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.message").isNotEmpty()
                .jsonPath("$.timestamp").isNotEmpty();
    }

    @Test
    void invalidLoginAndPassword() {
        String username = "kjkdkjdkjsd";
        String password = "sdk;lskkslk";

        var request = AuthRequest.builder()
                .username(username)
                .password(password)
                .build();

        webClient.post()
                .uri(URI_LOGIN)
                .bodyValue(request)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .jsonPath("$.message").isNotEmpty()
                .jsonPath("$.timestamp").isNotEmpty();
    }

    @Test
    void invalidPassword() {
        String password = "sdk;lskkslk";

        var request = AuthRequest.builder()
                .username(testUser.getName())
                .password(password)
                .build();

        webClient.post()
                .uri(URI_LOGIN)
                .bodyValue(request)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .jsonPath("$.message").isNotEmpty()
                .jsonPath("$.timestamp").isNotEmpty();
    }
}
