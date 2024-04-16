package hh.forest_of_habits.service.impl;

import hh.forest_of_habits.dto.request.ForestRequest;
import hh.forest_of_habits.dto.response.ForestResponse;
import hh.forest_of_habits.entity.Forest;
import hh.forest_of_habits.entity.User;
import hh.forest_of_habits.exception.ForestNotFoundException;
import hh.forest_of_habits.exception.InternalServerErrorException;
import hh.forest_of_habits.exception.UserNotFoundException;
import hh.forest_of_habits.mapper.ForestMapper;
import hh.forest_of_habits.repository.ForestRepository;
import hh.forest_of_habits.repository.UserRepository;
import hh.forest_of_habits.service.AuthFacade;
import hh.forest_of_habits.service.ForestService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
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
                .orElseThrow(() -> new UserNotFoundException(username));

        Forest forest = mapper.map(forestRequest);
        forest.setUser(user);

        try {
            return mapper.map(forestRepository.save(forest));
        } catch (DataAccessException e) {
            throw new InternalServerErrorException();
        }
    }

    @Override
    public ForestResponse change(Long id, ForestRequest forestRequest) {
        getForest(id);
        Forest changedForest = mapper.map(forestRequest);
        changedForest.setId(id);
        try {
            return mapper.map(forestRepository.save(changedForest));
        } catch (DataAccessException e) {
            throw new InternalServerErrorException();
        }
    }

    @Override
    public void delete(Long id) {
        getForest(id);
        try {
            forestRepository.deleteById(id);
        } catch (DataAccessException e) {
            throw new InternalServerErrorException();
        }
    }

    @Override
    public Forest getForest(Long id) {
        Forest forest = forestRepository.findById(id)
                .orElseThrow(() -> new ForestNotFoundException(id));
        auth.checkOwn(forest);
        return forest;
    }
}
