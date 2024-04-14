package hh.forest_of_habits.service.impl;

import hh.forest_of_habits.dto.request.IncrementationRequest;
import hh.forest_of_habits.dto.response.TreeFullResponse;
import hh.forest_of_habits.dto.request.TreeRequest;
import hh.forest_of_habits.dto.response.TreeResponse;
import hh.forest_of_habits.entity.Forest;
import hh.forest_of_habits.entity.Incrementation;
import hh.forest_of_habits.entity.Tree;
import hh.forest_of_habits.exception.NotFoundException;
import hh.forest_of_habits.exception.TreeNotFoundException;
import hh.forest_of_habits.mapper.IncrementationMapper;
import hh.forest_of_habits.mapper.TreeMapper;
import hh.forest_of_habits.repository.ForestRepository;
import hh.forest_of_habits.repository.IncrementationRepository;
import hh.forest_of_habits.repository.TreeRepository;
import hh.forest_of_habits.service.AuthFacade;
import hh.forest_of_habits.service.ForestService;
import hh.forest_of_habits.service.TreeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TreeServiceImpl implements TreeService {
    private final ForestService forestService;
    private final TreeRepository treeRepository;
    private final IncrementationRepository incrementationRepository;
    private final AuthFacade auth;
    private final TreeMapper treeMapper;
    private final IncrementationMapper incrementationMapper;

    @Override
    public List<TreeResponse> getAllByForestId(Long forestId) {
        Forest forest = forestService.getForest(forestId);
        return treeMapper.mapAll(forest.getTrees());
    }

    @Override
    public TreeFullResponse getById(Long id) {
        Tree tree = getTree(id);

        return mapToTreeFullResponse(tree);
    }

    @Override
    public TreeResponse create(TreeRequest treeRequest) {
        Forest forest = forestService.getForest(treeRequest.getForestId());
        Tree tree = treeMapper.map(treeRequest);
        tree.setForest(forest);
        Tree savedTree = treeRepository.save(tree);

        return mapToTreeResponse(savedTree);
    }

    @Override
    public TreeResponse update(Long id, TreeRequest treeRequest) {
        Tree tree = getTree(id);

        Optional.ofNullable(treeRequest.getCreatedAt()).ifPresent(tree::setCreatedAt);
        Optional.ofNullable(treeRequest.getDescription()).ifPresent(tree::setDescription);
        Optional.ofNullable(treeRequest.getLimit()).ifPresent(tree::setLimit);
        Optional.ofNullable(treeRequest.getPeriod()).ifPresent(tree::setPeriod);
        Optional.ofNullable(treeRequest.getName()).ifPresent(tree::setName);

        Tree savedTree = treeRepository.save(tree);

        return mapToTreeResponse(savedTree);
    }

    @Override
    public void delete(Long id) {
        Tree tree = getTree(id);
        treeRepository.delete(tree);
    }

    @Override
    public TreeFullResponse addIncrementation(IncrementationRequest incrementationRequest, Long treeId) {
        getTree(treeId);
        Incrementation incrementation = incrementationMapper.map(incrementationRequest);
        incrementation.setTreeId(treeId);
        incrementationRepository.save(incrementation);
        return getById(treeId);
    }

    private Tree getTree(Long treeId) {
        Tree tree = treeRepository.findById(treeId)
                .orElseThrow(() -> new TreeNotFoundException(treeId));
        auth.checkOwn(tree);
        return tree;
    }

    private TreeResponse mapToTreeResponse(Tree tree) {
        TreeResponse response = treeMapper.mapToShort(tree);
        response.setCounter(tree.getIncrementations()
                .stream()
                .mapToInt(Incrementation::getValue)
                .sum());
        return response;
    }

    private TreeFullResponse mapToTreeFullResponse(Tree tree) {
        TreeFullResponse response = treeMapper.mapToFull(tree);
        response.setIncrements(incrementationMapper.mapAll(tree.getIncrementations()));
        return response;
    }
}
