package hh.forest_of_habits.mapper;

import hh.forest_of_habits.dto.request.ForestRequest;
import hh.forest_of_habits.dto.response.ForestResponse;
import hh.forest_of_habits.entity.Forest;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ForestMapper {
    public abstract List<ForestResponse> mapAll(List<Forest> s);
    public abstract ForestResponse map(Forest s);
    public abstract Forest map(ForestRequest s);
}
