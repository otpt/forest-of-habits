package hh.forest_of_habits.service.impl;

import hh.forest_of_habits.dto.ForestDTO;
import hh.forest_of_habits.entity.Forest;
import hh.forest_of_habits.entity.User;
import hh.forest_of_habits.exception.NotFoundException;
import hh.forest_of_habits.mapper.SimpleMapper;
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

    @Override
    public List<ForestDTO> getAll() {
        String username = auth.getUsername();
        List<Forest> forests = forestRepository.findByUser_name(username);
        return forests.stream()
                .map(SimpleMapper::map)
                .toList();
    }

    @Override
    public ForestDTO getById(Long id) {
        Forest forest = getForest(id);
        return SimpleMapper.map(forest);
    }

    @Override
    public ForestDTO create(ForestDTO forestDTO) {
        String username = auth.getUsername();
        //TODO Можно взять из токена если его туда положить
        User user = userRepository.findByName(username)
                .orElseThrow(() -> new NotFoundException("Пользователь с username " + username + " не найден"));

        Forest forest = SimpleMapper.map(forestDTO);
        forest.setId(null);
        forest.setUser(user);

        return SimpleMapper.map(forestRepository.save(forest));
    }

    @Override
    public ForestDTO change(Long id, ForestDTO forestDTO) {
        Forest changedForest = SimpleMapper.map(forestDTO);
        changedForest.setId(id);
        return SimpleMapper.map(forestRepository.save(changedForest));
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
