package hh.forest_of_habits.service.impl;

import hh.forest_of_habits.dto.IncrementationDto;
import hh.forest_of_habits.dto.TreeFullDto;
import hh.forest_of_habits.dto.TreeNewDto;
import hh.forest_of_habits.dto.TreeShortDto;
import hh.forest_of_habits.entity.Forest;
import hh.forest_of_habits.entity.Tree;
import hh.forest_of_habits.exception.NotFoundException;
import hh.forest_of_habits.mapper.IncrementationMapper;
import hh.forest_of_habits.mapper.TreeMapper;
import hh.forest_of_habits.repository.ForestRepository;
import hh.forest_of_habits.repository.IncrementationRepository;
import hh.forest_of_habits.repository.TreeRepository;
import hh.forest_of_habits.service.TreeService;
import hh.forest_of_habits.utils.CheckOwnUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TreeServiceImpl implements TreeService {

    private final ForestRepository forestRepository;
    private final TreeRepository treeRepository;
    private final IncrementationRepository incrementationRepository;

    @Override
    public List<TreeShortDto> getAllByForestId(Long forestId) {
        Forest forest = forestRepository.findById(forestId)
                .orElseThrow(() -> new NotFoundException("Лес с id " + forestId + " не найден"));
        CheckOwnUtils.checkOwn(forest);
        return forest.getTrees()
                .stream()
                .map(TreeMapper::toTreeShortDto)
                .toList();
    }

    @Override
    public TreeFullDto getById(Long id) {
        Tree tree = getTree(id);
        CheckOwnUtils.checkOwn(tree.getForest());
        return TreeMapper.toTreeFullDto(tree);
    }

    @Override
    public TreeShortDto create(TreeNewDto dto) {
        Forest forest = forestRepository.findById(dto.getForestId())
                .orElseThrow(() -> new NotFoundException("Лес с id " + dto.getForestId() + " не найден"));
        CheckOwnUtils.checkOwn(forest);
        Tree savedTree = treeRepository.save(TreeMapper.toTree(dto, forest));
        return TreeMapper.toTreeShortDto(savedTree);
    }

    @Override
    public TreeShortDto update(Long id, TreeNewDto dto) {
        Tree tree = getTree(id);
        CheckOwnUtils.checkOwn(tree.getForest());

        Optional.ofNullable(dto.getCreatedAt()).ifPresent(tree::setCreatedAt);
        Optional.ofNullable(dto.getDescription()).ifPresent(tree::setDescription);
        Optional.ofNullable(dto.getLimit()).ifPresent(tree::setLimit);
        Optional.ofNullable(dto.getPeriod()).ifPresent(tree::setPeriod);
        Optional.ofNullable(dto.getName()).ifPresent(tree::setName);

        return TreeMapper.toTreeShortDto(treeRepository.save(tree));
    }

    @Override
    public void delete(Long id) {
        Tree tree = getTree(id);
        CheckOwnUtils.checkOwn(tree.getForest());
        treeRepository.delete(tree);
    }

    @Override
    public TreeFullDto addIncrementation(IncrementationDto dto, Long treeId) {
        CheckOwnUtils.checkOwn(getTree(treeId).getForest());
        incrementationRepository.save(IncrementationMapper.toModel(dto, treeId));
        return getById(treeId);
    }

    private Tree getTree(Long treeId) {
        return treeRepository.findById(treeId)
                .orElseThrow(() -> new NotFoundException(String.format("Дерево с id = %d не найдено", treeId)));
    }
}
