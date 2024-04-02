package hh.forest_of_habits.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ErrorDTO {
    private int code;
    private String message;
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
}
