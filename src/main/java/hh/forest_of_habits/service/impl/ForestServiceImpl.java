package hh.forest_of_habits.service.impl;

import hh.forest_of_habits.dto.request.ForestRequest;
import hh.forest_of_habits.dto.response.ForestResponse;
import hh.forest_of_habits.dto.response.TreeResponse;
import hh.forest_of_habits.entity.Forest;
import hh.forest_of_habits.entity.User;
import hh.forest_of_habits.exception.ForbiddenException;
import hh.forest_of_habits.exception.ForestNotFoundException;
import hh.forest_of_habits.exception.SharedObjectNotFoundException;
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
import java.util.UUID;

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
        Forest forest = forestRepository.findById(id)
                .orElseThrow(() -> new ForestNotFoundException(id));
        try {
            OwnUtils.checkOwn(forest);
        } catch (ForbiddenException e) {
            User user = userRepository.findByName(AuthFacade.getUsername()).get();
            if (forestRepository.checkPermission(user.getId(), id) == 0)
                throw new ForbiddenException(e.getMessage());
        }
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
    public UUID makeShared(Long id, boolean state) {
        Forest forest = getForest(id);
        forest.setSharedId(state ? UUID.randomUUID() : null);
        return forestRepository.save(forest).getSharedId();
    }

    @Override
    public void makeShared(Long forestId, Long userId, boolean state) {
        getForest(forestId);
        if (state) {
            forestRepository.insertAccess(userId, forestId);
        } else {
            forestRepository.deleteAccess(userId, forestId);
        }
    }

    @Override
    public void delete(Long id) {
        forestRepository.delete(getForest(id));
    }

    @Override
    public ForestResponse getByUuid(UUID id) {
        return forestRepository.findBySharedId(id).map(mapper::map)
                .orElseThrow(() -> new SharedObjectNotFoundException("Лес", id.toString()));
    }

    @Override
    public List<ForestResponse> getFriendsForests() {
        User user = userRepository.findByName(AuthFacade.getUsername()).get();
        List<Forest> forests = forestRepository.findFriendsForest(user.getId());
        return mapper.mapAll(forests);
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
}
