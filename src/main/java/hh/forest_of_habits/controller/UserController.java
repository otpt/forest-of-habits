package hh.forest_of_habits.controller;

import hh.forest_of_habits.dto.response.StatResponse;
import hh.forest_of_habits.dto.response.UserInfoResponse;
import hh.forest_of_habits.service.ForestService;
import hh.forest_of_habits.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ForestService forestService;

    @GetMapping("/me")
    public UserInfoResponse getUserInfo() {
        return userService.getUserInfo();
    }

    @GetMapping("/stat")
    public StatResponse getStat() { return forestService.getStat(); }
}
