package hh.forest_of_habits.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ForestResponse {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
 }
