package hh.forest_of_habits.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TreeIncrementsResponse extends TreeBaseResponse {
    private List<IncrementsDateValue> incrementsDateValue;
}
