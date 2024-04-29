package hh.forest_of_habits.service.impl;

import hh.forest_of_habits.dto.request.ForestRequest;
import hh.forest_of_habits.dto.response.ForestResponse;
import hh.forest_of_habits.entity.Forest;
import hh.forest_of_habits.entity.User;
import hh.forest_of_habits.exception.ForestNotFoundException;
import hh.forest_of_habits.exception.UserNotFoundException;
import hh.forest_of_habits.mapper.ForestMapper;
import hh.forest_of_habits.repository.ForestRepository;
import hh.forest_of_habits.repository.UserRepository;
import hh.forest_of_habits.service.ForestService;
import hh.forest_of_habits.utils.AuthFacade;
import hh.forest_of_habits.utils.OwnUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ForestServiceImpl implements ForestService {
    final ForestRepository forestRepository;
    final UserRepository userRepository;
    final ForestMapper mapper;

    @Override
    public List<ForestResponse> getAll() {
        String username = AuthFacade.getUsername();
        return mapper.mapAll(forestRepository.findByUser_name(username));
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
}
