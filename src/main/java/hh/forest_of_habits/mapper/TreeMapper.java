package hh.forest_of_habits.mapper;

import hh.forest_of_habits.dto.request.TreeRequest;
import hh.forest_of_habits.dto.response.IncrementsDateValue;
import hh.forest_of_habits.dto.response.TreeBaseResponse;
import hh.forest_of_habits.dto.response.TreeFullResponse;
import hh.forest_of_habits.dto.response.TreeIncrementsResponse;
import hh.forest_of_habits.dto.response.TreeResponse;
import hh.forest_of_habits.entity.Incrementation;
import hh.forest_of_habits.entity.Tree;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Mapper(componentModel = "spring", uses = IncrementationMapper.class,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class TreeMapper {
    public Tree map(TreeRequest request) {
        Tree tree = new Tree();
        update(tree, request);
        return tree;
    }

    public abstract void update(@MappingTarget Tree tree, TreeRequest request);

    public abstract TreeFullResponse mapToFull(Tree s);

    protected abstract void setFromTree(@MappingTarget TreeBaseResponse response, Tree tree);

    public TreeResponse mapToShort(Tree tree) {
        TreeResponse response = new TreeResponse();
        setFromTree(response, tree);
        response.setCounter(tree.getIncrements()
                .stream()
                .mapToInt(Incrementation::getValue)
                .sum());
        return response;
    }

    public TreeIncrementsResponse mapToIncrements(Tree tree) {
        TreeIncrementsResponse response = new TreeIncrementsResponse();
        setFromTree(response, tree);
        var dateSumMap = tree.getIncrements().stream().collect(
                groupingBy(
                        increment -> increment.getDate().toLocalDate(),
                        TreeMap::new,
                        Collectors.summingInt(Incrementation::getValue)));
        var dateSumList = dateSumMap.entrySet().stream()
                .map(entry -> new IncrementsDateValue(entry.getKey(), entry.getValue()))
                .toList();
        response.setIncrementsDateValue(dateSumList);
        return response;
    }

    public abstract List<TreeResponse> mapAll(List<Tree> s);
}