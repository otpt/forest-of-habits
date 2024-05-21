package hh.forest_of_habits.service;

import hh.forest_of_habits.dto.request.IncrementationRequest;
import hh.forest_of_habits.dto.request.TreeStatus;
import hh.forest_of_habits.dto.response.TreeIncrementsResponse;
import hh.forest_of_habits.dto.response.TreeFullResponse;
import hh.forest_of_habits.dto.request.TreeRequest;
import hh.forest_of_habits.dto.response.TreeResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public interface TreeService {
    List<TreeResponse> getAllByForestId(Long forestId, TreeStatus status);

    TreeFullResponse getById(@Positive(message = "id не может быть меньше 1") Long id);

    TreeResponse create(@Valid TreeRequest treeRequest);

    TreeResponse update(@Positive(message = "id не может быть меньше 1") Long id, @Valid TreeRequest treeRequest);

    void delete(@Positive(message = "id не может быть меньше 1") Long id);

    TreeIncrementsResponse addIncrementation(@Valid IncrementationRequest incrementationRequest,
                                             @Positive(message = "id не может быть меньше 1") Long treeId);
}