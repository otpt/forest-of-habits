package hh.forest_of_habits.controller;

import hh.forest_of_habits.dto.response.UserInfoResponse;
import hh.forest_of_habits.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public UserInfoResponse getUserInfo() {
        return userService.getUserInfo();
    }
}
