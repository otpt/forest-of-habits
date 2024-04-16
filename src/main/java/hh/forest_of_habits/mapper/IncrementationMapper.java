package hh.forest_of_habits.mapper;

import hh.forest_of_habits.dto.request.IncrementationRequest;
import hh.forest_of_habits.dto.response.IncrementationResponse;
import hh.forest_of_habits.entity.Incrementation;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class IncrementationMapper {
    public abstract IncrementationResponse map(Incrementation s);

    public abstract Incrementation map(IncrementationRequest s);

    public abstract List<IncrementationResponse> mapAll(List<Incrementation> s);
}