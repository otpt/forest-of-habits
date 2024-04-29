package hh.forest_of_habits.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import hh.forest_of_habits.enums.TreePeriod;
import hh.forest_of_habits.enums.TreeType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TreeRequest {
    private String name;
    private String description;
    private TreeType type;
    private TreePeriod period;
    private Integer limit;
    @JsonProperty("forest_id")
    private Long forestId;
}
