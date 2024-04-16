package hh.forest_of_habits.dto.request;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AuthRequest {
    private String username;
    private String password;
}
