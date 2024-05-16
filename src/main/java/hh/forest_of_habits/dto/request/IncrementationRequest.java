package hh.forest_of_habits.dto.request;

import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
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
    @Positive(message = "Значение инкрементации не может быть меньше 1")
    private Integer value;
}