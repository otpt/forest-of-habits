package hh.forest_of_habits.mapper;

import hh.forest_of_habits.dto.request.TreeRequest;
import hh.forest_of_habits.dto.response.TreeFullResponse;
import hh.forest_of_habits.dto.response.TreeResponse;
import hh.forest_of_habits.entity.Incrementation;
import hh.forest_of_habits.entity.Tree;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class TreeMapper {

    @Named(value = "used")
    public Tree map(TreeRequest s) {
        Tree tree = mapToTree(s);
        tree.setIncrementations(new ArrayList<>());
        return tree;
    }

    abstract Tree mapToTree(TreeRequest s);

    public abstract TreeFullResponse mapToFull(Tree s);

    @Named(value = "used")
    public TreeResponse mapToShort(Tree s) {
        TreeResponse response = mapToBase(s);
        response.setCounter(s.getIncrementations()
                .stream()
                .mapToInt(Incrementation::getValue)
                .sum());
        return response;
    }

    abstract TreeResponse mapToBase(Tree s);

    public List<TreeResponse> mapAll(List<Tree> s) {
        if (s == null) return null;
        if (s.isEmpty()) return List.of();
        return s.stream().map(this::mapToShort).toList();
    }
}