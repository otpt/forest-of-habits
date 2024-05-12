package hh.forest_of_habits;

import hh.forest_of_habits.dto.request.ForestRequest;
import hh.forest_of_habits.dto.request.RegistrationRequest;
import hh.forest_of_habits.entity.Forest;
import hh.forest_of_habits.entity.Tree;
import hh.forest_of_habits.enums.TreeType;
import hh.forest_of_habits.repository.ForestRepository;
import hh.forest_of_habits.repository.IncrementationRepository;
import hh.forest_of_habits.repository.TreeRepository;
import hh.forest_of_habits.repository.UserRepository;
import hh.forest_of_habits.service.AuthService;
import hh.forest_of_habits.service.ForestService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

public class TreeTests extends ForestOfHabitsApplicationTests {

    private static final String URI = "/tree";

    @BeforeEach
    void setUp(@Autowired AuthService authService,
               @Autowired ForestService forestService) {
        var token = authService.registration(RegistrationRequest.builder()
                .username(testUser.getName())
                .password(testUser.getPassword())
                .email(testUser.getEmail())
                .agreementConfirmation(testUser.getAgreementConfirmation())
                .build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                token.getUserName(),
                null,
                List.of()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        forestService.create(ForestRequest.builder()
                .name("forestName")
                .build());
        webClient = webClient.mutate()
                .defaultHeaders(httpHeaders -> httpHeaders.setBearerAuth(token.getToken()))
                .build();
    }

    @AfterEach
    void tearDown(@Autowired UserRepository userRepository,
                  @Autowired IncrementationRepository incrementationRepository,
                  @Autowired TreeRepository treeRepository,
                  @Autowired ForestRepository forestRepository) {
        incrementationRepository.deleteAll();
        treeRepository.deleteAll();
        forestRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Получение всех деревьев из леса")
    void getTrees() {
        long forestId = 1L;
        webClient.get()
                .uri(URI + "/by_forest/" + forestId)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.length").isEqualTo(1);
    }

    @Test
    @DisplayName("Получение всех деревьев из чужого леса")
    void getTreesForbidden() {
        long forestId = 2L;
        webClient.get()
                .uri(URI + "/by_forest/" + forestId)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @DisplayName("Получение всех деревьев из несуществующего леса")
    void getTreesNotFound() {
        long forestId = -1L;
        webClient.get()
                .uri(URI + "/by_forest/" + forestId)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @DisplayName("Получение всех деревьев из леса неавторизованным пользователем")
    void getTreesUnauthorized() {
        long forestId = -1L;

        unauthorize();

        webClient.get()
                .uri(URI + "/by_forest/" + forestId)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    @DisplayName("Получение дерева по id")
    void getTreeById() {
        long id = 1L;

        webClient.get()
                .uri(URI + "/" + id)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.length").isEqualTo(1);
    }

    @Test
    @DisplayName("Получение чужого дерева по id")
    void getTreeByIdForbidden() {
        long id = 1L;

        webClient.get()
                .uri(URI + "/" + id)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @DisplayName("Получение несуществующего дерева по id ")
    void getTreeByIdNotFound() {
        long id = 1L;

        webClient.get()
                .uri(URI + "/" + id)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @DisplayName("Создание булева дерева")
    void createBooleanTree() {
        webClient
                .get()
                .uri(URI)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$.[0].id").isNumber()
                .jsonPath("$.[0].name").isNotEmpty()
                .jsonPath("$.[0].createdAt").isNotEmpty()
                .jsonPath("$.[0].trees").isArray();
    }

    @Test
    @DisplayName("Создание булева дерева")
    void createTree() {
        webClient
                .get()
                .uri(URI)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$.[0].id").isNumber()
                .jsonPath("$.[0].name").isNotEmpty()
                .jsonPath("$.[0].createdAt").isNotEmpty()
                .jsonPath("$.[0].trees").isArray();
    }

    @Test
    @DisplayName("Удаление дерева")
    void deleteTree() {
        long id = 1L;

        webClient
                .delete()
                .uri(URI + "/" + id)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("Удаление чужого дерева")
    void deleteTreeForbidden() {
        long id = 2L;

        webClient
                .delete()
                .uri(URI + "/" + id)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("Удаление несуществующего дерева")
    void deleteTreeNotFound() {
        long id = 100L;

        webClient
                .delete()
                .uri(URI + "/" + id)
                .exchange()
                .expectStatus().isOk();
    }

    void unauthorize() {
        webClient = webClient.mutate()
                .defaultHeaders(httpHeaders -> httpHeaders.remove(HttpHeaders.AUTHORIZATION))
                .build();
    }
}
