package hh.forest_of_habits.service.impl;

import hh.forest_of_habits.dto.IncrementationDto;
import hh.forest_of_habits.dto.TreeFullDto;
import hh.forest_of_habits.dto.TreeShortDto;
import hh.forest_of_habits.entity.Incrementation;
import hh.forest_of_habits.exception.NotFoundException;
import hh.forest_of_habits.mapper.IncrementationMapper;
import hh.forest_of_habits.mapper.TreeMapper;
import hh.forest_of_habits.repository.ForestRepository;
import hh.forest_of_habits.repository.IncrementationRepository;
import hh.forest_of_habits.repository.TreeRepository;
import hh.forest_of_habits.service.TreeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TreeServiceImpl implements TreeService {

    private final ForestRepository forestRepository;
    private final TreeRepository treeRepository;
    private final IncrementationRepository incrementationRepository;

    @Override
    public List<TreeShortDto> getAllByForestId(Long forestId) {
        if (!forestRepository.existsById(forestId)) {
            throw new NotFoundException(String.format("Лес с id = %d не найден", forestId));
        }
        return treeRepository.findAllByForestId(forestId).stream().map(tree -> {
            List<Incrementation> incrementations = incrementationRepository.findAllByTreeId(tree.getId());
            return TreeMapper.toTreeShortDto(tree, incrementations.stream().mapToInt(Incrementation::getValue).sum());
        }).toList();
    }

    @Override
    public TreeFullDto getById(Long id) {
        return TreeMapper.toTreeFullDto(treeRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException(String.format("Дерево с id = %d не найдено", id))),
                incrementationRepository.findAllByTreeId(id));
    }

    @Override
    public TreeShortDto create(TreeShortDto dto) {
        if (!forestRepository.existsById(dto.getForestId())) {
            throw new NotFoundException(String.format("Лес с id = %d не найден", dto.getForestId()));
        }
        return TreeMapper.toTreeShortDto(treeRepository.save(TreeMapper.toTree(dto)), 0);
    }

    @Override
    public TreeShortDto update(Long id, TreeShortDto dto) {
        if (!treeRepository.existsById(id)) {
            throw new NotFoundException(String.format("Дерево с id = %d не найдено", id));
        }
        if (!forestRepository.existsById(dto.getForestId())) {
            throw new NotFoundException(String.format("Лес с id = %d не найден", dto.getForestId()));
        }
        return TreeMapper.toTreeShortDto(treeRepository.save(TreeMapper.toTree(dto, id)),
                incrementationRepository.findAllByTreeId(id).stream().mapToInt(Incrementation::getValue).sum());
    }

    @Override
    public void delete(Long id) {
        if (!treeRepository.existsById(id)) {
            throw new NotFoundException(String.format("Дерево с id = %d не найдено", id));
        }
        forestRepository.deleteById(id);
    }

    @Override
    public TreeFullDto addIncrementation(IncrementationDto dto, Long treeId) {
        if (!treeRepository.existsById(treeId)) {
            throw new NotFoundException(String.format("Дерево с id = %d не найдено", treeId));
        }
        incrementationRepository.save(IncrementationMapper.toModel(dto, treeId));
        return getById(treeId);
    }
}
