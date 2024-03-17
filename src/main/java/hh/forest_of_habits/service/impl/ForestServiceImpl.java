package hh.forest_of_habits.service.impl;

import hh.forest_of_habits.dto.ForestDTO;
import hh.forest_of_habits.entity.Forest;
import hh.forest_of_habits.exception.NotFoundException;
import hh.forest_of_habits.mapper.SimpleMapper;
import hh.forest_of_habits.repository.ForestRepository;
import hh.forest_of_habits.service.ForestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ForestServiceImpl implements ForestService {
    final ForestRepository forestRepository;
    final SimpleMapper mapper;
    @Override
    public List<ForestDTO> getAll() {
        List<Forest> forests = forestRepository.findAll();
        return forests.stream()
                .map(mapper::map)
                .toList();
    }

    @Override
    public ForestDTO getById(Long id) {
        Forest forest = forestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Лес с id " + id + " не найден"));
        return mapper.map(forest);
    }

    @Override
    public ForestDTO create(ForestDTO forestDTO) {
        Forest forest = mapper.map(forestDTO);
        forest.setId(null);
        return mapper.map(forestRepository.save(forest));
    }

    @Override
    public ForestDTO change(Long id, ForestDTO forestDTO) {
        forestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Лес с id " + id + " не найден"));
        Forest changedForest = mapper.map(forestDTO);
        changedForest.setId(id);
        return mapper.map(forestRepository.save(changedForest));
    }

    @Override
    public void delete(Long id) {
        forestRepository.deleteById(id);
    }
}
