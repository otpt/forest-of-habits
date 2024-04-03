package hh.forest_of_habits.service.impl;

import hh.forest_of_habits.dto.IncrementationDto;
import hh.forest_of_habits.dto.TreeFullDto;
import hh.forest_of_habits.dto.TreeNewDto;
import hh.forest_of_habits.dto.TreeShortDto;
import hh.forest_of_habits.entity.Forest;
import hh.forest_of_habits.entity.Tree;
import hh.forest_of_habits.exception.ForbiddenException;
import hh.forest_of_habits.exception.NotFoundException;
import hh.forest_of_habits.mapper.IncrementationMapper;
import hh.forest_of_habits.mapper.TreeMapper;
import hh.forest_of_habits.repository.ForestRepository;
import hh.forest_of_habits.repository.IncrementationRepository;
import hh.forest_of_habits.repository.TreeRepository;
import hh.forest_of_habits.service.AuthFacade;
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
    private final AuthFacade auth;

    @Override
    public List<TreeShortDto> getAllByForestId(Long forestId) {
        checkForestOwn(forestId);
        return treeRepository.findAllByForestId(forestId)
                .stream()
                .map(TreeMapper::toTreeShortDto)
                .toList();
    }

    @Override
    public TreeFullDto getById(Long id) {
        Tree tree = treeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Дерево с id = %d не найдено", id)));
        checkForestOwn(tree.getForestId());
        return TreeMapper.toTreeFullDto(tree);
    }

    @Override
    public TreeShortDto create(TreeNewDto dto) {
        checkForestOwn(dto.getForestId());
        Tree savedTree = treeRepository.save(TreeMapper.toTree(dto));
        savedTree.setIncrementations(List.of());
        return TreeMapper.toTreeShortDto(savedTree);
    }

    @Override
    public TreeShortDto update(Long id, TreeNewDto dto) {
        checkOwn(id);
        Tree tree = treeRepository.findById(id).get();

        if (dto.getCreatedAt() != null) tree.setCreatedAt(dto.getCreatedAt());
        if (dto.getDescription() != null) tree.setDescription(dto.getDescription());
        if (dto.getLimit() != null) tree.setLimit(dto.getLimit());
        if (dto.getPeriod() != null) tree.setPeriod(dto.getPeriod());
        if (dto.getName() != null) tree.setName(dto.getName());

        return TreeMapper.toTreeShortDto(treeRepository.save(tree));
    }

    @Override
    public void delete(Long id) {
        checkOwn(id);
        treeRepository.deleteById(id);
    }

    @Override
    public TreeFullDto addIncrementation(IncrementationDto dto, Long treeId) {
        checkOwn(treeId);
        incrementationRepository.save(IncrementationMapper.toModel(dto, treeId));
        return getById(treeId);
    }

    private void checkForestOwn(Long forestId) {
        Forest forest = forestRepository.findById(forestId)
                .orElseThrow(() -> new NotFoundException(String.format("Лес с id = %d не найдено", forestId)));

        String username = auth.getUsername();
        if (!forest.getUser().getName().equals(username))
            throw new ForbiddenException("Нет доступа");
    }

    private void checkOwn(Long treeId) {
        Tree tree = treeRepository.findById(treeId)
                .orElseThrow(() -> new NotFoundException(String.format("Дерево с id = %d не найдено", treeId)));

        Forest forest = forestRepository.findById(tree.getForestId())
                .orElseThrow(() -> new NotFoundException(String.format("Лес с id = %d не найдено", tree.getForestId())));

        String username = auth.getUsername();
        if (!forest.getUser().getName().equals(username))
            throw new ForbiddenException("Нет доступа");
    }
}
