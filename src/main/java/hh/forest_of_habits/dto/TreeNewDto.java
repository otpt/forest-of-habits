package hh.forest_of_habits.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import hh.forest_of_habits.enums.TreePeriod;
import hh.forest_of_habits.enums.TreeType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TreeNewDto {
    private String name;
    private String description;
    private TreeType type;
    private TreePeriod period;
    private LocalDateTime createdAt;
    private Integer limit;
    @JsonProperty("forest_id")
    private Long forestId;
}
