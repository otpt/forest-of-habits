package hh.forest_of_habits.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class IncrementationDto {
    private Long id;
    private LocalDateTime date;
    private Integer value;
}