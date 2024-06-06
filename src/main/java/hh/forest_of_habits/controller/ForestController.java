package hh.forest_of_habits.controller;

import hh.forest_of_habits.dto.request.ForestRequest;
import hh.forest_of_habits.dto.response.ForestResponse;
import hh.forest_of_habits.service.ForestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/forest")
public class ForestController {
    final ForestService forestService;

    @GetMapping
    public List<ForestResponse> getAll() {
        return forestService.getAll();
    }

    @PostMapping
    ForestResponse create(@RequestBody ForestRequest forestRequest) {
        return forestService.create(forestRequest);
    }

    @GetMapping("/{id}")
    ForestResponse getById(@PathVariable Long id) {
        return forestService.getById(id);
    }

    @PutMapping("/{id}")
    ForestResponse change(@PathVariable Long id, @RequestBody ForestRequest forestRequest) {
        return forestService.change(id, forestRequest);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        forestService.delete(id);
    }

    @GetMapping("/friends")
    public List<ForestResponse> getFriendsForests() {
        return forestService.getFriendsForests();
    }

    @PutMapping("/share/{id}")
    UUID makeShared(@PathVariable Long id) {
        return forestService.makeShared(id, true);
    }

    @PutMapping("/share")
    void makeShared(@RequestParam Long forestId, @RequestParam Long userId) {
        forestService.makeShared(forestId, userId, true);
    }

    @DeleteMapping("/share/{id}")
    void makeUnshared(@PathVariable Long id) {
        forestService.makeShared(id, false);
    }

    @DeleteMapping("/share")
    void makeUnshared(@RequestParam Long forestId, @RequestParam Long userId) {
        forestService.makeShared(forestId, userId, false);
    }
}