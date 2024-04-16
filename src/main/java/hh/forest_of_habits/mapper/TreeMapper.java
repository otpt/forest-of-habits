package hh.forest_of_habits.mapper;

import hh.forest_of_habits.dto.request.TreeRequest;
import hh.forest_of_habits.dto.response.TreeFullResponse;
import hh.forest_of_habits.dto.response.TreeResponse;
import hh.forest_of_habits.entity.Tree;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class TreeMapper {
    public abstract Tree map(TreeRequest s);

    public abstract TreeFullResponse mapToFull(Tree s);
    public abstract TreeResponse mapToShort(Tree s);

    public abstract List<TreeResponse> mapAll(List<Tree> s);
}