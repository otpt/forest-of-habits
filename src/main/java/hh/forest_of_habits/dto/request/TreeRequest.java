package hh.forest_of_habits.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import hh.forest_of_habits.enums.TreePeriod;
import hh.forest_of_habits.enums.TreeType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TreeRequest {
    @NotBlank(message = "Название дерева не может быть пустым")
    private String name;
    private String description;
    @NotNull(message = "Тип дерева должен быть указан")
    private TreeType type;
    private TreePeriod period;
    private Integer limit;
    @JsonProperty("forest_id")
    @Positive(message = "ID леса, к которому относится дерево, должен быть больше или равен 1")
    private Long forestId;
}
