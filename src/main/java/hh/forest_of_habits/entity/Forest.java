package hh.forest_of_habits.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "forests")
public class Forest {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "forests_seq_generator")
    @SequenceGenerator(name = "forests_seq_generator", sequenceName = "forests_seq_generator", allocationSize = 1)
    @Column(name = "forest_id")
    private Long id;

    @Column(name = "forest_name")
    private String name;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
