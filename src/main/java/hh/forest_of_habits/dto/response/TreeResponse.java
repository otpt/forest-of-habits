package hh.forest_of_habits.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "trees_with_increments_for_period")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TreeResponse extends TreeBaseResponse {
    @Column(name = "inc_count")
    private Integer counter;
}