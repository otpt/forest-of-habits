package hh.forest_of_habits;

import hh.forest_of_habits.dto.request.ForestRequest;
import hh.forest_of_habits.dto.request.IncrementationRequest;
import hh.forest_of_habits.dto.request.RegistrationRequest;
import hh.forest_of_habits.dto.request.TreeRequest;
import hh.forest_of_habits.dto.response.ForestResponse;
import hh.forest_of_habits.dto.response.TreeResponse;
import hh.forest_of_habits.entity.Forest;
import hh.forest_of_habits.entity.Tree;
import hh.forest_of_habits.enums.TreePeriod;
import hh.forest_of_habits.enums.TreeType;
import hh.forest_of_habits.repository.ForestRepository;
import hh.forest_of_habits.repository.IncrementationRepository;
import hh.forest_of_habits.repository.TreeRepository;
import hh.forest_of_habits.repository.UserRepository;
import hh.forest_of_habits.service.AuthService;
import hh.forest_of_habits.service.ForestService;
import hh.forest_of_habits.service.TreeService;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

public class ForestTests extends ForestOfHabitsApplicationTests {

    private static final String URI = "/forest";

    private final List<Forest> testForests = List.of(
            Forest.builder()
                    .name("testForest1")
                    .trees(List.of(
                            Tree.builder()
                                    .name("boolean_tree1")
                                    .type(TreeType.BOOLEAN_TREE)
                                    .description("description1")
                                    .increments(List.of())
                                    .build()
                    ))
                    .build()
    );

    @BeforeEach
    void setUp(@Autowired AuthService authService,
               @Autowired ForestService forestService,
               @Autowired TreeService treeService) {
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

        ForestResponse forest1 = forestService.create(ForestRequest.builder()
                .name("Forest1")
                .build());

        TreeResponse booleanEndTree = treeService.create(TreeRequest.builder()
                .type(TreeType.BOOLEAN_TREE)
                .name("BooleanEndTree")
                .description("description")
                .forestId(forest1.getId())
                .build());

        TreeResponse booleanTree = treeService.create(TreeRequest.builder()
                .type(TreeType.BOOLEAN_TREE)
                .name("BooleanTree")
                .description("description")
                .forestId(forest1.getId())
                .build());

        TreeResponse unlimitedTree = treeService.create(TreeRequest.builder()
                .type(TreeType.UNLIMITED_TREE)
                .name("unlimitedTree")
                .description("description")
                .forestId(forest1.getId())
                .build());

        TreeResponse unlimitedTree2 = treeService.create(TreeRequest.builder()
                .type(TreeType.UNLIMITED_TREE)
                .name("unlimitedTree2")
                .description("description")
                .forestId(forest1.getId())
                .build());

        TreeResponse limitedEndTree = treeService.create(TreeRequest.builder()
                .type(TreeType.LIMITED_TREE)
                .name("limitedTree")
                .description("description")
                .limit(4)
                .forestId(forest1.getId())
                .build());

        TreeResponse limitedTree = treeService.create(TreeRequest.builder()
                .type(TreeType.LIMITED_TREE)
                .name("limitedTree")
                .description("description")
                .limit(5)
                .forestId(forest1.getId())
                .build());

        TreeResponse periodicTree = treeService.create(TreeRequest.builder()
                .type(TreeType.PERIODIC_TREE)
                .name("periodicTree")
                .description("description")
                .period(TreePeriod.WEEK)
                .forestId(forest1.getId())
                .build());

        treeService.addIncrementation(IncrementationRequest.builder()
                .date(LocalDateTime.now())
                .value(1)
                .build(), booleanEndTree.getId());

        Stream.iterate(0, i -> i + 1)
                .limit(4)
                .forEach(i -> treeService.addIncrementation(IncrementationRequest.builder()
                        .date(LocalDateTime.now().minusDays(i))
                        .value(1)
                        .build(), unlimitedTree.getId()));

        Stream.iterate(0, i -> i + 1)
                .limit(limitedEndTree.getLimit())
                .forEach(i -> treeService.addIncrementation(IncrementationRequest.builder()
                        .date(LocalDateTime.now().minusDays(i))
                        .value(1)
                        .build(), limitedEndTree.getId()));

        Stream.iterate(0, i -> i + 1)
                .limit(2)
                .forEach(i -> treeService.addIncrementation(IncrementationRequest.builder()
                        .date(LocalDateTime.now().plusWeeks(i))
                        .value(1)
                        .build(), periodicTree.getId()));


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
    @DisplayName("Успешное создание леса")
    void createForest() {
        String forestName = "myForest";
        var forest = ForestRequest.builder()
                .name(forestName)
                .build();
        webClient.post()
                .uri(URI)
                .bodyValue(forest)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isNumber()
                .jsonPath("$.name").isEqualTo(forestName)
                .jsonPath("$.createdAt").isNotEmpty()
                .jsonPath("$.trees").isArray();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Валидация данных при создании леса")
    void createForestBadRequest(String forestName) {
        var forest = ForestRequest.builder()
                .name(forestName)
                .build();
        webClient.post()
                .uri(URI)
                .bodyValue(forest)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.message").isNotEmpty()
                .jsonPath("$.timestamp").isNotEmpty();
    }

    @Test
    @DisplayName("Неверное тело запроса при создании леса")
    void createForestInvalidBody() {
        String json = """
                {
                    "someKey": "someValue"
                }
                """;
        webClient.post()
                .uri(URI)
                .bodyValue(json)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.message").isNotEmpty()
                .jsonPath("$.timestamp").isNotEmpty();
    }

    @Test
    @DisplayName("Неавторизованное создание леса")
    void createForestUnauthorized() {
        String forestName = "ForestName";

        var forest = ForestRequest.builder()
                .name(forestName)
                .build();

        unauthorize();

        webClient
                .post()
                .uri(URI)
                .bodyValue(forest)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    @DisplayName("Получение всех лесов")
    void getAllForests() {
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
    @DisplayName("Получение всех лесов неавторизованного пользователя")
    void getAllForestsUnautorized() {
        unauthorize();

        webClient
                .get()
                .uri(URI)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    @DisplayName("Получение леса по id")
    void getAllForestById() {
        webClient
                .get()
                .uri(URI + "/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$.id").isNumber()
                .jsonPath("$.name").isNotEmpty()
                .jsonPath("$.createdAt").isNotEmpty()
                .jsonPath("$.trees").isArray();
    }

    @Test
    @DisplayName("Получение чужого леса")
    void getAllForestByIdForbidden() {
        webClient
                .get()
                .uri(URI + "/2")
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @DisplayName("Получение несуществующего леса")
    void getAllForestByIdNotFound() {
        webClient
                .get()
                .uri(URI + "/-1")
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @DisplayName("Получение леса по id не авторизованного пользователя")
    void getAllForestByIdUnauthorized() {
        unauthorize();

        webClient
                .get()
                .uri(URI + "/1")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    @DisplayName("Удаление леса")
    void deleteForest() {
        webClient
                .delete()
                .uri(URI + "/1")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("Удаление несуществующего леса")
    void deleteForestDoesntExists() {
        webClient
                .delete()
                .uri(URI + "/-1")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @DisplayName("Удаление леса, который не принадлежит пользователю")
    void deleteForestForbidden() {
        webClient
                .delete()
                .uri(URI + "/2")
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @DisplayName("Изменение леса")
    void changeForest() {
        webClient
                .put()
                .uri(URI + "/2")
                .bodyValue(null)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$.id").isNumber()
                .jsonPath("$.name").isNotEmpty()
                .jsonPath("$.createdAt").isNotEmpty()
                .jsonPath("$.trees").isArray();
    }

    @Test
    @DisplayName("Изменение не существующего леса")
    void changeForestNotFound() {
        webClient
                .put()
                .uri(URI + "/2")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @DisplayName("Изменение чужого леса")
    void changeForestForbidden() {
        webClient
                .put()
                .uri(URI + "/2")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @DisplayName("Изменение леса, некорректные данные")
    void changeForestBadRequest() {
        webClient
                .put()
                .uri(URI + "/2")
                .exchange()
                .expectStatus().isBadRequest();
    }

    void unauthorize() {
        webClient = webClient.mutate()
                .defaultHeaders(httpHeaders -> httpHeaders.remove(HttpHeaders.AUTHORIZATION))
                .build();
    }
}
