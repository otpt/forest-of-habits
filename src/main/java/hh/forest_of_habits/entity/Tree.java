package hh.forest_of_habits.entity;

import hh.forest_of_habits.enums.TreePeriod;
import hh.forest_of_habits.enums.TreeType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "trees")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorColumn(name = "tree_type", discriminatorType = DiscriminatorType.STRING)
public class Tree {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @Column(name = "tree_type", insertable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private TreeType type;

    @Column
    @Enumerated(EnumType.STRING)
    private TreePeriod period;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column
    private Integer limit;

    @Column(name = "forest_id")
    private Long forestId;
}