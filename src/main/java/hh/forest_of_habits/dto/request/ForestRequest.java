package hh.forest_of_habits.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForestRequest {
    @NotBlank(message = "Название леса не может быть пустым")
    private String name;
}
