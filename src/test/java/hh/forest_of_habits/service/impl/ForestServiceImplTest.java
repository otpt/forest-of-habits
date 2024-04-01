package hh.forest_of_habits.service.impl;

import hh.forest_of_habits.dto.ForestDTO;
import hh.forest_of_habits.entity.Forest;
import hh.forest_of_habits.entity.User;
import hh.forest_of_habits.exception.ForbiddenException;
import hh.forest_of_habits.exception.NotFoundException;
import hh.forest_of_habits.repository.ForestRepository;
import hh.forest_of_habits.repository.UserRepository;
import hh.forest_of_habits.service.AuthFacade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ForestServiceImplTest {
    @Mock
    ForestRepository forestRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    AuthFacade auth;
    @InjectMocks
    ForestServiceImpl testing;

    @Test
    @DisplayName("Получение списка лесов")
    public void getAll() {
        String username = "name";

        List<Forest> list = List.of(
                Forest.builder().build(),
                Forest.builder().build(),
                Forest.builder().build(),
                Forest.builder().build(),
                Forest.builder().build()
        );

        when(auth.getUsername()).thenReturn(username);
        when(forestRepository.findByUser_name(username)).thenReturn(list);

        var actual = testing.getAll();
        assertEquals(list.size(), actual.size());
    }

    @Test
    @DisplayName("Создание леса")
    public void createForest() {
        String username = "name";

        User user = new User();
        user.setName(username);

        when(userRepository.findByName(username)).thenReturn(Optional.of(user));
        when(forestRepository.save(any(Forest.class))).thenAnswer(i -> i.getArgument(0));
        when(auth.getUsername()).thenReturn(username);


        String name = "name";
        ForestDTO dto = ForestDTO.builder()
                .name(name)
                .build();

        var actual = testing.create(dto);
        assertThat(actual.getCreatedAt()).isNotNull();
        assertEquals(name, actual.getName());
    }
    @Test
    @DisplayName("Получение леса по id")
    public void getForestById() {
        String username = "name";
        Long forestId = 1L;

        User user = new User();
        user.setName(username);

        String forestName = "name";
        Forest forest = Forest.builder()
                .id(forestId)
                .name(forestName)
                .user(user)
                .build();

        when(auth.getUsername()).thenReturn(username);
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
        String username = "name";
        String anotherUsername = "anotherName";
        Long forestId = 1L;

        User user = new User();
        user.setName(username);

        String forestName = "name";
        Forest forest = Forest.builder()
                .id(forestId)
                .name(forestName)
                .user(user)
                .build();

        when(forestRepository.findById(anyLong())).thenReturn(Optional.of(forest));
        when(auth.getUsername()).thenReturn(anotherUsername);

        assertThrows(ForbiddenException.class, () -> testing.getById(forestId));
    }


    @Test
    @DisplayName("Изменение леса")
    public void changeForest() {
        String username = "name";
        String name = "forest";
        Long id = 1L;

        User user = new User();
        user.setName(username);

        Forest forest = Forest.builder()
                .name(name)
                .user(user)
                .build();

        when(forestRepository.findById(id)).thenReturn(Optional.of(forest));
        when(forestRepository.save(any(Forest.class))).thenAnswer(i -> i.getArgument(0));
        when(auth.getUsername()).thenReturn(username);


        String anotherName = "forest2";
        ForestDTO changes = ForestDTO.builder()
                .name(anotherName)
                .build();

        var actual = testing.change(id, changes);
        assertThat(actual.getCreatedAt()).isNotNull();
        assertEquals(anotherName, actual.getName());
    }

    @Test
    @DisplayName("Изменение леса, которого нет в базе")
    public void changeForestIfNotExist() {
        Long id = 1L;
        when(forestRepository.findById(id)).thenReturn(Optional.empty());

        String anotherName = "forest2";
        ForestDTO changes = ForestDTO.builder()
                .name(anotherName)
                .build();

        assertThrows(NotFoundException.class, () -> testing.change(id, changes));
    }

    @Test
    @DisplayName("Изменение леса, который не принадлежит пользователю")
    public void changeForestIfNotOwn() {
        String username = "name";
        String name = "forest";
        Long id = 1L;

        User user = new User();
        user.setName(username);

        Forest forest = Forest.builder()
                .name(name)
                .user(user)
                .build();

        String anotherUsername = "anotherName";
        when(forestRepository.findById(id)).thenReturn(Optional.of(forest));
        when(auth.getUsername()).thenReturn(anotherUsername);


        String anotherName = "forest2";
        ForestDTO changes = ForestDTO.builder()
                .name(anotherName)
                .build();

        assertThrows(ForbiddenException.class, () -> testing.change(id, changes));
    }
}