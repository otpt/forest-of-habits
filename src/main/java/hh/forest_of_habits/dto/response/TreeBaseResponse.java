package hh.forest_of_habits.dto.response;

import hh.forest_of_habits.enums.TreePeriod;
import hh.forest_of_habits.enums.TreeType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public abstract class TreeBaseResponse {
    private Long id;
    private String name;
    private String description;
    private TreeType type;
    private TreePeriod period;
    private LocalDateTime createdAt;
    private Integer limit;
    private Long forestId;
}
