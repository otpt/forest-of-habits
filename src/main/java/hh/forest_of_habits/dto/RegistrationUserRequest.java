package hh.forest_of_habits.dto;

import lombok.Data;

@Data
public class RegistrationUserRequest {
    private String username;
    private String password;
    private String email;
}
