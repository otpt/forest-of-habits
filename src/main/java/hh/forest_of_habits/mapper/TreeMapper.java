package hh.forest_of_habits.mapper;

import hh.forest_of_habits.dto.response.TreeBaseResponse;
import hh.forest_of_habits.dto.request.TreeRequest;
import hh.forest_of_habits.dto.response.TreeResponse;
import hh.forest_of_habits.entity.Tree;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class TreeMapper {
    public abstract Tree map(TreeRequest s);

    public abstract TreeBaseResponse map(Tree s);

    public abstract List<TreeResponse> mapAll(List<Tree> s);
}