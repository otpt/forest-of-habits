package hh.forest_of_habits.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "incrementation_log")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Incrementation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tree_id")
    private Long treeId;

    @CreationTimestamp
    private LocalDateTime date;

    private Integer value;
}