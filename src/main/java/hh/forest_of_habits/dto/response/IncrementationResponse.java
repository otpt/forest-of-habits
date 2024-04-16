package hh.forest_of_habits.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class IncrementationResponse {
    private Long id;
    private LocalDateTime date;
    private Integer value;
}