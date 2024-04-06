package hh.forest_of_habits.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class ErrorResponse {
    private String message;
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
}
