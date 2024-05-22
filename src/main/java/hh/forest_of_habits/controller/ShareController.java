package hh.forest_of_habits.controller;

import hh.forest_of_habits.dto.response.ForestResponse;
import hh.forest_of_habits.service.ForestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/shared")
public class ShareController {
    private final ForestService forestService;

    @GetMapping("/{id}")
    ForestResponse getForest(@PathVariable UUID id) {
        return forestService.getByUUID(id);
    }

    @PutMapping("/{id}")
    UUID makeShared(@PathVariable Long id) {
        return forestService.makeShared(id, true);
    }

    @PutMapping
    void makeShared(@RequestParam Long forestId, @RequestParam Long userId) {
        forestService.makeShared(forestId, userId, true);
    }

    @DeleteMapping("/{id}")
    void makeUnshared(@PathVariable Long id) {
        forestService.makeShared(id, false);
    }

    @DeleteMapping
    void makeUnshared(@RequestParam Long forestId, @RequestParam Long userId) {
        forestService.makeShared(forestId, userId, false);
    }
}