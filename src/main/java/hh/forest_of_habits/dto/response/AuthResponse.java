package hh.forest_of_habits.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AuthResponse {
    private String token;
    private String userName;
    private String email;
}
