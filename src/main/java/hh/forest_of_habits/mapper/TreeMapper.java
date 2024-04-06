package hh.forest_of_habits.mapper;
import hh.forest_of_habits.dto.TreeFullDto;
import hh.forest_of_habits.dto.TreeNewDto;
import hh.forest_of_habits.dto.TreeShortDto;
import hh.forest_of_habits.entity.Forest;
import hh.forest_of_habits.entity.Incrementation;
import hh.forest_of_habits.entity.Tree;
import hh.forest_of_habits.enums.TreePeriod;
import hh.forest_of_habits.enums.TreeType;

import java.time.LocalDateTime;

public class TreeMapper {

    public static Tree toTree(TreeNewDto dto, Forest forest) {
        return Tree.builder()
                .id(null)
                .name(dto.getName())
                .createdAt(dto.getCreatedAt() == null ? LocalDateTime.now() : dto.getCreatedAt())
                .description(dto.getDescription())
                .limit(dto.getLimit())
                .type(dto.getType())
                .period(dto.getType() != TreeType.PERIODIC_TREE ? TreePeriod.NONE : dto.getPeriod())
                .forest(forest)
                .build();
    }

    public static TreeShortDto toTreeShortDto(Tree tree) {
        return TreeShortDto.builder()
                .id(tree.getId())
                .name(tree.getName())
                .createdAt(tree.getCreatedAt() == null ? LocalDateTime.now() : tree.getCreatedAt())
                .description(tree.getDescription())
                .limit(tree.getLimit())
                .type(tree.getType())
                .period(tree.getPeriod())
                .counter(tree.getIncrementations().stream().mapToInt(Incrementation::getValue).sum())
                .forestId(tree.getForest().getId())
                .build();
    }

    public static TreeFullDto toTreeFullDto(Tree tree) {
        return TreeFullDto.builder()
                .id(tree.getId())
                .name(tree.getName())
                .createdAt(tree.getCreatedAt() == null ? LocalDateTime.now() : tree.getCreatedAt())
                .description(tree.getDescription())
                .limit(tree.getLimit())
                .type(tree.getType())
                .period(tree.getPeriod())
                .increments(tree.getIncrementations().stream().map(IncrementationMapper::toDto).toList())
                .forestId(tree.getForest().getId())
                .build();
    }
}