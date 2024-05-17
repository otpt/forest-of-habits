package hh.forest_of_habits.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserInfoResponse {
    String username;
    String email;
}
