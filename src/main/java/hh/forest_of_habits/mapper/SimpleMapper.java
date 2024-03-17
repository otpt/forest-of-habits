package hh.forest_of_habits.mapper;

import hh.forest_of_habits.dto.ForestDTO;
import hh.forest_of_habits.entity.Forest;
import org.springframework.stereotype.Component;

@Component
public class SimpleMapper {
    public Forest map(ForestDTO forestDTO) {
        return Forest.builder()
                .id(forestDTO.getId())
                .name(forestDTO.getName())
                .createdAt(forestDTO.getCreatedAt())
                .build();
    }

    public ForestDTO map(Forest forest) {
        return ForestDTO.builder()
                .id(forest.getId())
                .name(forest.getName())
                .createdAt(forest.getCreatedAt())
                .build();
    }
}
