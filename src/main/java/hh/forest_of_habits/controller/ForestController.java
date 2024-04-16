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
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

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
}