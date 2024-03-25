package hh.forest_of_habits.dto;

import lombok.Data;

@Data
public class RegistrationRequest {
    private String username;
    private String password;
    private String email;
}
