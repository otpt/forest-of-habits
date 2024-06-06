package hh.forest_of_habits.service;

import hh.forest_of_habits.dto.request.ForestRequest;
import hh.forest_of_habits.dto.response.ForestResponse;
import hh.forest_of_habits.entity.Forest;
import hh.forest_of_habits.entity.User;
import hh.forest_of_habits.exception.ForbiddenException;
import hh.forest_of_habits.exception.NotFoundException;
import hh.forest_of_habits.mapper.ForestMapper;
import hh.forest_of_habits.repository.ForestRepository;
import hh.forest_of_habits.repository.TreeRepository;
import hh.forest_of_habits.repository.UserRepository;
import hh.forest_of_habits.service.impl.ForestServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ForestServiceTest {
    @Mock
    ForestRepository forestRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    TreeRepository treeRepository;
    @Mock
    ForestMapper mapper;
    @InjectMocks
    ForestServiceImpl testing;

    String username = "name";


    @BeforeEach
    public void setUp() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, "");

        SecurityContext context = new SecurityContextImpl();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @Test
    @DisplayName("Получение списка лесов")
    public void getAll() {

        List<Forest> list = List.of(
                Forest.builder().build(),
                Forest.builder().build(),
                Forest.builder().build()
        );

        when(forestRepository.findByUser_name(username)).thenReturn(list);
        when(mapper.mapAll(anyList())).thenReturn(List.of(
                new ForestResponse(),
                new ForestResponse(),
                new ForestResponse()
        ));

        var actual = testing.getAll();
        assertEquals(list.size(), actual.size());
    }

    @Test
    @DisplayName("Создание леса")
    public void createForest() {
        User user = new User();
        user.setName(username);

        String name = "name";
        ForestRequest dto = ForestRequest.builder()
                .name(name)
                .build();
        ForestResponse response = new ForestResponse();
        response.setName(name);

        when(mapper.map(any(ForestRequest.class))).thenReturn(new Forest());
        when(userRepository.findByName(username)).thenReturn(Optional.of(user));
        when(forestRepository.save(any(Forest.class))).thenAnswer(i -> i.getArgument(0));
        when(mapper.map(any(Forest.class))).thenReturn(response);

        var actual = testing.create(dto);
        assertEquals(name, actual.getName());
    }

    @Test
    @DisplayName("Получение леса по id")
    public void getForestById() {
        Long forestId = 1L;

        User user = new User();
        user.setName(username);

        String forestName = "name";
        Forest forest = Forest.builder()
                .id(forestId)
                .name(forestName)
                .user(user)
                .build();

        ForestResponse response = new ForestResponse();
        response.setName(forestName);

        when(mapper.map(any(Forest.class))).thenReturn(response);
        when(forestRepository.findById(anyLong())).thenReturn(Optional.of(forest));

        var actual = testing.getById(forestId);

        assertEquals(forestName, actual.getName());
    }

    @Test
    @DisplayName("Получение леса, которого нет в базе")
    public void getForestByIdIfNotExists() {
        Long forestId = 1L;
        when(forestRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> testing.getById(forestId));
    }

    @Test
    @DisplayName("Получение леса, который не принадлежит пользователю")
    public void getForestByIdIfNotOwn() {
        String anotherUsername = "anotherName";
        Long forestId = 1L;

        User user = new User();
        user.setName(anotherUsername);

        when(userRepository.findByName(any())).thenReturn(Optional.of(user));

        String forestName = "name";
        Forest forest = Forest.builder()
                .id(forestId)
                .name(forestName)
                .user(user)
                .build();

        when(forestRepository.findById(anyLong())).thenReturn(Optional.of(forest));

        assertThrows(ForbiddenException.class, () -> testing.getById(forestId));
    }


    @Test
    @DisplayName("Изменение леса")
    public void changeForest() {
        String name = "forest";
        Long id = 1L;

        User user = new User();
        user.setName(username);

        Forest forest = Forest.builder()
                .id(id)
                .name(name)
                .user(user)
                .build();

        String anotherName = "forest2";
        ForestRequest changes = ForestRequest.builder()
                .name(anotherName)
                .build();

        var response = ForestResponse.builder()
                .id(id)
                .name(anotherName)
                .build();

        when(forestRepository.findById(id)).thenReturn(Optional.of(forest));
        when(mapper.map(any(Forest.class))).thenReturn(response);

        var actual = testing.change(id, changes);

        Mockito.verify(mapper, Mockito.times(1))
                .update(any(Forest.class), any(ForestRequest.class));
        assertEquals(actual.getId(), forest.getId());
        assertEquals(anotherName, actual.getName());
    }

    @Test
    @DisplayName("Изменение леса, которого нет в базе")
    public void changeForestIfNotExist() {
        Long id = 1L;
        when(forestRepository.findById(id)).thenReturn(Optional.empty());

        String anotherName = "forest2";
        ForestRequest changes = ForestRequest.builder()
                .name(anotherName)
                .build();

        assertThrows(NotFoundException.class, () -> testing.change(id, changes));
    }

    @Test
    @DisplayName("Изменение леса, который не принадлежит пользователю")
    public void changeForestIfNotOwn() {
        String anotherUsername = "anotherName";
        String name = "forest";
        Long id = 1L;

        User user = new User();
        user.setName(anotherUsername);

        Forest forest = Forest.builder()
                .name(name)
                .user(user)
                .build();

        when(forestRepository.findById(id)).thenReturn(Optional.of(forest));

        String anotherName = "forest2";
        ForestRequest changes = ForestRequest.builder()
                .name(anotherName)
                .build();

        assertThrows(ForbiddenException.class, () -> testing.change(id, changes));
    }
}