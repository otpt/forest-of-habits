package hh.forest_of_habits.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import hh.forest_of_habits.enums.TreePeriod;
import hh.forest_of_habits.enums.TreeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TreeShortDto {
    private Long id;
    private String name;
    private String description;
    private TreeType type;
    private TreePeriod period;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    private Integer limit;
    @JsonProperty("forest_id")
    private Long forestId;
    private Integer counter;
}