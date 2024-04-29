package hh.forest_of_habits.service;

import hh.forest_of_habits.dto.request.IncrementationRequest;
import hh.forest_of_habits.dto.response.TreeFullResponse;
import hh.forest_of_habits.dto.request.TreeRequest;
import hh.forest_of_habits.dto.response.TreeResponse;

import java.util.List;

public interface TreeService {
    List<TreeResponse> getAllByForestId(Long forestId);

    TreeFullResponse getById(Long id);

    TreeResponse create(TreeRequest treeRequest);

    TreeResponse update(Long id, TreeRequest treeRequest);

    void delete(Long id);

    TreeResponse addIncrementation(IncrementationRequest incrementationRequest, Long treeId);
}