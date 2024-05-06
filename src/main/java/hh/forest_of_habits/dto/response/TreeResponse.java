package hh.forest_of_habits.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import hh.forest_of_habits.enums.TreePeriod;
import hh.forest_of_habits.enums.TreeType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class TreeResponse extends TreeBaseResponse {
    private Integer counter;

    public TreeResponse(Long id, String name, String description, TreeType type, TreePeriod period, LocalDateTime createdAt, Integer limit, Long forestId, Long counter) {
        super.setId(id);
        super.setName(name);
        super.setDescription(description);
        super.setType(type);
        super.setPeriod(period);
        super.setCreatedAt(createdAt);
        super.setLimit(limit);
        super.setForestId(forestId);
        this.counter = counter.intValue();
    }
}