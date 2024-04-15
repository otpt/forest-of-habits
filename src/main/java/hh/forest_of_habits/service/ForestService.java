package hh.forest_of_habits.service;

import hh.forest_of_habits.dto.request.ForestRequest;
import hh.forest_of_habits.dto.response.ForestResponse;

import java.util.List;

public interface ForestService {
    List<ForestResponse> getAll();

    ForestResponse getById(Long id);

    ForestResponse create(ForestRequest forestRequest);

    ForestResponse change(Long id, ForestRequest forestRequest);

    void delete(Long id);
}
