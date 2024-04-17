package hh.forest_of_habits.service.impl;

import hh.forest_of_habits.dto.request.ForestRequest;
import hh.forest_of_habits.dto.response.ForestResponse;
import hh.forest_of_habits.entity.Forest;
import hh.forest_of_habits.entity.Incrementation;
import hh.forest_of_habits.entity.Tree;
import hh.forest_of_habits.entity.User;
import hh.forest_of_habits.exception.BadRequestException;
import hh.forest_of_habits.exception.NotFoundException;
import hh.forest_of_habits.mapper.ForestMapper;
import hh.forest_of_habits.mapper.TreeMapper;
import hh.forest_of_habits.repository.ForestRepository;
import hh.forest_of_habits.repository.UserRepository;
import hh.forest_of_habits.service.AuthFacade;
import hh.forest_of_habits.service.ForestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ForestServiceImpl implements ForestService {
    final ForestRepository forestRepository;
    final UserRepository userRepository;
    final AuthFacade auth;
    final ForestMapper mapper;
    final TreeMapper treeMapper;

    @Override
    public List<ForestResponse> getAll() {
        String username = auth.getUsername();
        List<Forest> forestList = forestRepository.findByUser_name(username);
        forestList.forEach(forest -> forest.setTrees(sort(forest.getTrees()).stream().limit(3).toList()));
        return mapper.mapAll(forestList);
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
        getForest(id);
        Forest changedForest = mapper.map(forestRequest);
        changedForest.setId(id);
        return mapper.map(forestRepository.save(changedForest));
    }

    @Override
    public void delete(Long id) {
        getForest(id);
        forestRepository.deleteById(id);
    }

    private Forest getForest(Long id) {
        Forest forest = forestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Лес с id " + id + " не найден"));
        auth.checkOwn(forest);
        return forest;
    }
    private List<Tree> sort(List<Tree> trees) {
        List<Tree> result = new ArrayList<>();
        trees.forEach(tree -> {
            boolean shouldAddFirst = false;
            switch (tree.getType()) {
                case BOOLEAN_TREE -> shouldAddFirst = tree.getIncrementations().isEmpty();
                case UNLIMITED_TREE -> shouldAddFirst = tree.getIncrementations()
                        .stream()
                        .filter(incrementation -> incrementation.getDate().toLocalDate().equals(LocalDate.now()))
                        .toList().isEmpty();
                case LIMITED_TREE -> shouldAddFirst = tree.getIncrementations()
                        .stream()
                        .mapToInt(Incrementation::getValue).sum() < tree.getLimit();
                case PERIODIC_TREE -> shouldAddFirst = isHaveIncrementInPeriod(tree);
            }
            if (shouldAddFirst) {
                result.add(0, tree);
            } else {
                result.add(tree);
            }
        });
        return result;
    }

    private boolean isHaveIncrementInPeriod(Tree tree) {
        LocalDate now = LocalDate.now();
        return tree.getIncrementations().stream()
                .filter(incrementation -> {
                    switch (tree.getPeriod()) {
                        case DAY -> {
                            return incrementation.getDate().toLocalDate().equals(now);
                        }
                        case WEEK -> {
                            LocalDate startOfWeek = now.minusDays(now.getDayOfWeek().getValue() - 1);
                            LocalDate endOfWeek = now.plusDays(7 - now.getDayOfWeek().getValue());
                            return incrementation.getDate().toLocalDate().isAfter(startOfWeek) && incrementation.getDate().toLocalDate().isBefore(endOfWeek);
                        }
                        case MONTH -> {
                            YearMonth currentMonth = YearMonth.of(now.getYear(), now.getMonth());
                            return YearMonth.of(incrementation.getDate().toLocalDate().getYear(), incrementation.getDate().toLocalDate().getMonth()).equals(currentMonth);
                        }
                        case QUARTER -> {
                            int currentQuarter = (now.getMonthValue() - 1) / 3 + 1;
                            int quarter = (incrementation.getDate().toLocalDate().getMonthValue() - 1) / 3 + 1;
                            return incrementation.getDate().toLocalDate().getYear() == now.getYear() && quarter == currentQuarter;
                        }
                        case YEAR -> {
                            return incrementation.getDate().toLocalDate().getYear() == now.getYear();
                        }
                        default -> throw new BadRequestException("Неизвестный тип периодичности");
                    }
                }).toList().isEmpty();
    }
}
