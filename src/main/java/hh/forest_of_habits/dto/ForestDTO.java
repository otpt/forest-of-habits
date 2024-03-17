package hh.forest_of_habits.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ForestDTO {
    private Long id;
    private String name;
    @Builder.Default
    @JsonProperty("created-at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
