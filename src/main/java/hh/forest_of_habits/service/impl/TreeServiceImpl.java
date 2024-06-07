package hh.forest_of_habits.service.impl;

import hh.forest_of_habits.dto.request.IncrementationRequest;
import hh.forest_of_habits.dto.request.TreeRequest;
import hh.forest_of_habits.dto.request.TreeStatus;
import hh.forest_of_habits.dto.response.TreeFullResponse;
import hh.forest_of_habits.dto.response.TreeIncrementsResponse;
import hh.forest_of_habits.dto.response.TreeResponse;
import hh.forest_of_habits.entity.Forest;
import hh.forest_of_habits.entity.Incrementation;
import hh.forest_of_habits.entity.Tree;
import hh.forest_of_habits.exception.SharedObjectNotFoundException;
import hh.forest_of_habits.exception.TreeNotFoundException;
import hh.forest_of_habits.mapper.IncrementationMapper;
import hh.forest_of_habits.mapper.TreeMapper;
import hh.forest_of_habits.repository.ForestRepository;
import hh.forest_of_habits.repository.IncrementationRepository;
import hh.forest_of_habits.repository.TreeRepository;
import hh.forest_of_habits.service.ForestService;
import hh.forest_of_habits.service.TreeService;
import hh.forest_of_habits.utils.OwnUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@SuppressWarnings("checkstyle:VariableDeclarationUsageDistance")
@RequiredArgsConstructor
@Service
public class TreeServiceImpl implements TreeService {
    private final ForestService forestService;
    private final TreeRepository treeRepository;
    private final ForestRepository forestRepository;
    private final IncrementationRepository incrementationRepository;
    private final TreeMapper treeMapper;
    private final IncrementationMapper incrementationMapper;

    @Override
    public List<TreeResponse> getAllByForestId(Long forestId, TreeStatus status) {
        Forest forest = forestService.getForest(forestId);
        return getFilteredTreesByForestId(forest.getId(), status);
    }

    @Override
    public List<TreeResponse> getAllByForestUuid(UUID forestUuid, TreeStatus status) {
        Forest forest = forestRepository.findBySharedId(forestUuid).orElseThrow(
                () -> new SharedObjectNotFoundException("Лес", forestUuid.toString())
        );
        return getFilteredTreesByForestId(forest.getId(), status);
    }

    @Override
    public TreeFullResponse getById(Long id) {
        Tree tree = treeRepository.findById(id)
                .orElseThrow(() -> new TreeNotFoundException(id));
        if (tree.getForest().getSharedId() == null) {
            OwnUtils.checkOwn(tree);
        }
        return treeMapper.mapToFull(tree);
    }

    @Override
    public TreeResponse create(TreeRequest treeRequest) {
        Forest forest = forestService.getForest(treeRequest.getForestId());
        Tree tree = treeMapper.map(treeRequest);
        tree.setForest(forest);
        Tree savedTree = treeRepository.save(tree);
        return treeMapper.mapToShort(savedTree);
    }

    @Override
    public TreeResponse update(Long id, TreeRequest treeRequest) {
        Tree tree = getTree(id);

        treeMapper.update(tree, treeRequest);
        Tree savedTree = treeRepository.save(tree);
        return treeMapper.mapToShort(savedTree);
    }

    @Override
    public void delete(Long id) {
        Tree tree = getTree(id);
        treeRepository.delete(tree);
    }

    @Override
    public TreeIncrementsResponse addIncrementation(IncrementationRequest incrementationRequest, Long treeId) {
        Tree tree = getTree(treeId);
        Incrementation incrementation = new Incrementation();
        incrementationMapper.update(incrementation, incrementationRequest);
        incrementation.setTreeId(treeId);
        incrementationRepository.save(incrementation);
        return treeMapper.mapToIncrements(tree);
    }

    private Tree getTree(Long treeId) {
        Tree tree = treeRepository.findById(treeId)
                .orElseThrow(() -> new TreeNotFoundException(treeId));
        OwnUtils.checkOwn(tree);
        return tree;
    }

    private List<TreeResponse> getOpenTrees(List<TreeResponse> trees) {
        return trees.stream()
                .filter(tree -> switch (tree.getType()) {
                    case BOOLEAN_TREE -> tree.getCounter() == 0;
                    case UNLIMITED_TREE, PERIODIC_TREE -> true;
                    case LIMITED_TREE -> tree.getCounter() < tree.getLimit();
                })
                .toList();
    }

    private List<TreeResponse> getCloseTrees(List<TreeResponse> trees) {
        return trees.stream()
                .filter(tree -> switch (tree.getType()) {
                    case BOOLEAN_TREE -> tree.getCounter() > 0;
                    case UNLIMITED_TREE, PERIODIC_TREE -> false;
                    case LIMITED_TREE -> tree.getCounter() >= tree.getLimit();
                })
                .toList();
    }

    private  List<TreeResponse> getFilteredTreesByForestId(Long forestId, TreeStatus status) {
        List<TreeResponse> allTrees = treeRepository.findTreesWithIncrementsCounter(forestId);
        return switch (status) {
            case ALL -> allTrees;
            case OPEN -> getOpenTrees(allTrees);
            case CLOSE -> getCloseTrees(allTrees);
        };
    }

}
