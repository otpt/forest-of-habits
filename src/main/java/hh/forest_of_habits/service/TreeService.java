package hh.forest_of_habits.service;
import hh.forest_of_habits.dto.IncrementationDto;
import hh.forest_of_habits.dto.TreeFullDto;
import hh.forest_of_habits.dto.TreeShortDto;

import java.util.List;

public interface TreeService {
    List<TreeShortDto> getAllByForestId(Long forestId);

    TreeFullDto getById(Long id);

    TreeShortDto create(TreeShortDto forest);

    TreeShortDto update(Long id, TreeShortDto forest);

    void delete(Long id);

    TreeFullDto addIncrementation(IncrementationDto dto, Long treeId);
}