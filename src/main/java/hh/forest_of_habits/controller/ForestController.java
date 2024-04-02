package hh.forest_of_habits.controller;

import hh.forest_of_habits.dto.ForestDTO;
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
    public List<ForestDTO> getAll() {
        return forestService.getAll();
    }

    @PostMapping
    ForestDTO create(@RequestBody ForestDTO forestDTO) {
        return forestService.create(forestDTO);
    }

    @GetMapping("/{id}")
    ForestDTO getById(@PathVariable Long id) {
        return forestService.getById(id);
    }

    @PutMapping("/{id}")
    ForestDTO change(@PathVariable Long id, @RequestBody ForestDTO forestDTO) {
        return forestService.change(id, forestDTO);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        forestService.delete(id);
    }
}