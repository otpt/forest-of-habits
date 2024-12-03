package hh.forest_of_habits.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
public class F2ARequest {
    @NotNull
    @Size(min = 6, max = 6, message = "Код должен содержать 6 цифр")
    String code;
}
