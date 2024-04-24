package hh.forest_of_habits.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ForestResponse {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private List<TreeResponse> trees;
 }
