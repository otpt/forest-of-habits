package hh.forest_of_habits.dto.request;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RegistrationRequest {
    private String username;
    private String password;
    private String email;
    private Boolean agreementConfirmation;
}
