package hh.forest_of_habits.mapper;

import hh.forest_of_habits.dto.request.IncrementationRequest;
import hh.forest_of_habits.dto.response.IncrementationResponse;
import hh.forest_of_habits.entity.Incrementation;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class IncrementationMapper {
    public abstract IncrementationResponse map(Incrementation s);

    public abstract Incrementation map(IncrementationRequest s);

    public abstract void update(@MappingTarget Incrementation incrementation, IncrementationRequest request);

    public abstract List<IncrementationResponse> mapAll(List<Incrementation> s);
}