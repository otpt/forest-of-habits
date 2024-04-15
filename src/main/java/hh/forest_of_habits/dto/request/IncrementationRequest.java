package hh.forest_of_habits.dto.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class IncrementationRequest {
    private Long id;
    private LocalDateTime date;
    private Integer value;
}