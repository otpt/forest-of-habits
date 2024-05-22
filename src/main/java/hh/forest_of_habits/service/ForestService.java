package hh.forest_of_habits.service;

import hh.forest_of_habits.dto.request.ForestRequest;
import hh.forest_of_habits.dto.response.ForestResponse;
import hh.forest_of_habits.entity.Forest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

@Validated
public interface ForestService {
    List<ForestResponse> getAll();

    ForestResponse getById(Long id);

    ForestResponse create(@Valid ForestRequest forestRequest);

    ForestResponse change(@Positive(message = "id не может быть меньше 1") Long id, @Valid ForestRequest forestRequest);

    UUID makeShared(Long id, boolean state);

    void makeShared(Long forestId, Long userId, boolean state);
  
    ForestResponse getByUUID(UUID id);

    List<ForestResponse> getFriendsForests();
  
    void delete(@Positive(message = "id не может быть меньше 1") Long id);

    Forest getForest(@Positive(message = "id не может быть меньше 1") Long id);
}
