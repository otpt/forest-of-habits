package hh.forest_of_habits.dto.response;

import hh.forest_of_habits.enums.TreePeriod;
import hh.forest_of_habits.enums.TreeType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public abstract class TreeBaseResponse {
    @Id
    private Long id;
    private String name;
    private String description;

    @Column(name = "tree_type")
    @Enumerated(EnumType.STRING)
    private TreeType type;

    @Enumerated(EnumType.STRING)
    private TreePeriod period;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "tree_limit")
    private Integer limit;

    @Column(name = "forest_id")
    private Long forestId;
}
