package hh.forest_of_habits.service;

import hh.forest_of_habits.dto.request.ForestRequest;
import hh.forest_of_habits.dto.response.ForestResponse;
import hh.forest_of_habits.entity.Forest;

import java.util.List;
import java.util.UUID;

public interface ForestService {
    List<ForestResponse> getAll();

    ForestResponse getById(Long id);

    ForestResponse create(ForestRequest forestRequest);

    ForestResponse change(Long id, ForestRequest forestRequest);

    UUID makeShared(Long id, boolean state);

    void makeShared(Long forestId, Long userId, boolean state);

    void delete(Long id);

    Forest getForest(Long id);

    ForestResponse getByUUID(UUID id);

    List<ForestResponse> getFriendsForests();
}
