package hh.forest_of_habits.service;

import hh.forest_of_habits.dto.ForestDTO;

import java.util.List;

public interface ForestService {
    List<ForestDTO> getAll();

    ForestDTO getById(Long id);

    ForestDTO create(ForestDTO forest);

    ForestDTO change(Long id, ForestDTO forest);

    void delete(Long id);
}
