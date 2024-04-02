package hh.forest_of_habits.mapper;
import hh.forest_of_habits.dto.TreeFullDto;
import hh.forest_of_habits.dto.TreeShortDto;
import hh.forest_of_habits.entity.Incrementation;
import hh.forest_of_habits.entity.Tree;

import java.time.LocalDateTime;
import java.util.List;

public class TreeMapper {
    public static Tree toTree(TreeShortDto dto, Long id) {
        return Tree.builder()
                .id(id)
                .name(dto.getName())
                .createdAt(dto.getCreatedAt() == null ? LocalDateTime.now() : dto.getCreatedAt())
                .description(dto.getDescription())
                .limit(dto.getLimit())
                .type(dto.getType())
                .period(dto.getPeriod())
                .build();
    }
    public static Tree toTree(TreeShortDto dto) {
        return Tree.builder()
                .id(dto.getId())
                .name(dto.getName())
                .createdAt(dto.getCreatedAt() == null ? LocalDateTime.now() : dto.getCreatedAt())
                .description(dto.getDescription())
                .limit(dto.getLimit())
                .type(dto.getType())
                .period(dto.getPeriod())
                .build();
    }

    public static TreeShortDto toTreeShortDto(Tree tree, Integer counter) {
        return TreeShortDto.builder()
                .id(tree.getId())
                .name(tree.getName())
                .createdAt(tree.getCreatedAt() == null ? LocalDateTime.now() : tree.getCreatedAt())
                .description(tree.getDescription())
                .limit(tree.getLimit())
                .type(tree.getType())
                .period(tree.getPeriod())
                .counter(counter)
                .build();
    }

    public static TreeFullDto toTreeFullDto(Tree tree, List<Incrementation> incrementations) {
        return TreeFullDto.builder()
                .id(tree.getId())
                .name(tree.getName())
                .createdAt(tree.getCreatedAt() == null ? LocalDateTime.now() : tree.getCreatedAt())
                .description(tree.getDescription())
                .limit(tree.getLimit())
                .type(tree.getType())
                .period(tree.getPeriod())
                .increments(incrementations.stream().map(IncrementationMapper::toDto).toList())
                .build();
    }
}