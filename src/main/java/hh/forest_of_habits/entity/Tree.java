package hh.forest_of_habits.entity;

import hh.forest_of_habits.enums.TreePeriod;
import hh.forest_of_habits.enums.TreeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trees")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tree {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @OneToMany
    @JoinColumn(name = "tree_id")
    private List<Incrementation> increments = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "forest_id")
    private Forest forest;
}