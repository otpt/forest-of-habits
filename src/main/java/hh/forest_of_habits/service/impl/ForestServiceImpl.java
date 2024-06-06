package hh.forest_of_habits.service.impl;

import hh.forest_of_habits.dto.request.ForestRequest;
import hh.forest_of_habits.dto.response.ForestResponse;
import hh.forest_of_habits.dto.response.StatResponse;
import hh.forest_of_habits.dto.response.TreeResponse;
import hh.forest_of_habits.entity.Forest;
import hh.forest_of_habits.entity.User;
import hh.forest_of_habits.exception.ForestNotFoundException;
import hh.forest_of_habits.exception.UserNotFoundException;
import hh.forest_of_habits.mapper.ForestMapper;
import hh.forest_of_habits.repository.ForestRepository;
import hh.forest_of_habits.repository.TreeRepository;
import hh.forest_of_habits.repository.UserRepository;
import hh.forest_of_habits.service.ForestService;
import hh.forest_of_habits.utils.AuthFacade;
import hh.forest_of_habits.utils.OwnUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ForestServiceImpl implements ForestService {

    private static final Integer TREE_LIST_LIMIT = 3;

    final ForestRepository forestRepository;
    final UserRepository userRepository;
    final ForestMapper mapper;
    final TreeRepository treeRepository;


    @Override
    public List<ForestResponse> getAll() {
        String username = AuthFacade.getUsername();
        List<ForestResponse> responseList = mapper.mapAll(forestRepository.findByUser_name(username));
        responseList.forEach(forestResponse ->
                forestResponse.setTrees(sort(
                        treeRepository.findTreesWithIncrementsCounter(forestResponse.getId()))
                        .stream().limit(TREE_LIST_LIMIT).toList()));
        return responseList;
    }

    @Override
    public ForestResponse getById(Long id) {
        Forest forest = getForest(id);
        return mapper.map(forest);
    }

    @Override
    public ForestResponse create(ForestRequest forestRequest) {
        String username = AuthFacade.getUsername();
        //TODO Можно взять из токена если его туда положить
        User user = userRepository.findByName(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        Forest forest = mapper.map(forestRequest);
        forest.setUser(user);
        return mapper.map(forestRepository.save(forest));
    }

    @Transactional
    @Override
    public ForestResponse change(Long id, ForestRequest forestRequest) {
        Forest forest = getForest(id);
        mapper.update(forest, forestRequest);
        return mapper.map(forest);
    }

    @Override
    public void delete(Long id) {
        forestRepository.delete(getForest(id));
    }

    @Override
    public Forest getForest(Long id) {
        Forest forest = forestRepository.findById(id)
                .orElseThrow(() -> new ForestNotFoundException(id));
        OwnUtils.checkOwn(forest);
        return forest;
    }

    private List<TreeResponse> sort(List<TreeResponse> trees) {
        List<TreeResponse> result = new ArrayList<>();
        trees.forEach(tree -> {
            boolean shouldAddFirst = false;
            switch (tree.getType()) {
                case BOOLEAN_TREE, PERIODIC_TREE -> shouldAddFirst = tree.getCounter() == 0;
                case UNLIMITED_TREE -> shouldAddFirst = true;
                case LIMITED_TREE -> shouldAddFirst = tree.getCounter() < tree.getLimit();
                default -> throw new IllegalArgumentException("Неизвестный тип дерева");
            }
            result.add(shouldAddFirst ? 0 : result.size(), tree);
        });
        return result;
    }

    @Override
    public StatResponse getStat() {
        List<Forest> forests = forestRepository.findByUser_name(AuthFacade.getUsername());
        int allForests = forests.size();
        int allTrees = 0;
        int openTrees = 0;
        int closeTrees = 0;
        int openForests = 0;
        int closeForests = 0;
        for (Forest forest : forests) {
            List<TreeResponse> trees = treeRepository.findTreesWithIncrementsCounter(forest.getId());
            int all = trees.size();
            int close = (int) trees.stream().filter(tree -> switch (tree.getType()) {
                case BOOLEAN_TREE -> tree.getCounter() > 0;
                case UNLIMITED_TREE, PERIODIC_TREE -> false;
                case LIMITED_TREE -> tree.getCounter() >= tree.getLimit();
            }).count();
            if (all != close) {
                openForests++;
            } else {
                closeForests++;
            }
            allTrees += all;
            closeTrees += close;
            openTrees += all - close;
        }
        return new StatResponse(allTrees, openTrees, closeTrees, allForests, openForests, closeForests);
    }
}
