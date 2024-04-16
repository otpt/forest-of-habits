package hh.forest_of_habits.service.impl;

import hh.forest_of_habits.dto.request.ForestRequest;
import hh.forest_of_habits.dto.response.ForestResponse;
import hh.forest_of_habits.entity.Forest;
import hh.forest_of_habits.entity.User;
import hh.forest_of_habits.exception.NotFoundException;
import hh.forest_of_habits.mapper.ForestMapper;
import hh.forest_of_habits.repository.ForestRepository;
import hh.forest_of_habits.repository.UserRepository;
import hh.forest_of_habits.service.AuthFacade;
import hh.forest_of_habits.service.ForestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ForestServiceImpl implements ForestService {
    final ForestRepository forestRepository;
    final UserRepository userRepository;
    final AuthFacade auth;
    final ForestMapper mapper;

    @Override
    public List<ForestResponse> getAll() {
        String username = auth.getUsername();
        return mapper.mapAll(forestRepository.findByUser_name(username));
    }

    @Override
    public ForestResponse getById(Long id) {
        Forest forest = getForest(id);
        return mapper.map(forest);
    }

    @Override
    public ForestResponse create(ForestRequest forestRequest) {
        String username = auth.getUsername();
        //TODO Можно взять из токена если его туда положить
        User user = userRepository.findByName(username)
                .orElseThrow(() -> new NotFoundException("Пользователь с username " + username + " не найден"));

        Forest forest = mapper.map(forestRequest);
        forest.setUser(user);

        return mapper.map(forestRepository.save(forest));
    }

    @Override
    public ForestResponse change(Long id, ForestRequest forestRequest) {
        Forest forest = getForest(id);
        Forest changedForest = mapper.map(forestRequest);
        changedForest.setId(id);
        return mapper.map(forestRepository.save(changedForest));
    }

    @Override
    public void delete(Long id) {
        forestRepository.deleteById(id);
    }

    private Forest getForest(Long id) {
        Forest forest = forestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Лес с id " + id + " не найден"));
        auth.checkOwn(forest);
        return forest;
    }
}
