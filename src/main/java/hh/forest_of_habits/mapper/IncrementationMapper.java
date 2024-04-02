package hh.forest_of_habits.mapper;

import hh.forest_of_habits.dto.IncrementationDto;
import hh.forest_of_habits.entity.Incrementation;

public class IncrementationMapper {
    public static IncrementationDto toDto(Incrementation model) {
        return IncrementationDto.builder()
                .id(model.getId())
                .date(model.getDate())
                .value(model.getValue())
                .build();
    }

    public static Incrementation toModel(IncrementationDto dto, Long treeId) {
        return Incrementation.builder()
                .id(dto.getId())
                .treeId(treeId)
                .date(dto.getDate())
                .value(dto.getValue())
                .build();
    }
}