package hh.forest_of_habits.dto.request;

import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncrementationRequest {
    @PastOrPresent(message = "Дата инкрементации не может быть в будущем")
    private LocalDateTime date;
    private Integer value;
}