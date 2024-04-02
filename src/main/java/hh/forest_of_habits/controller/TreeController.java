package hh.forest_of_habits.controller;

import hh.forest_of_habits.dto.IncrementationDto;
import hh.forest_of_habits.dto.TreeFullDto;
import hh.forest_of_habits.dto.TreeShortDto;
import hh.forest_of_habits.service.TreeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/tree")
public class TreeController {
    private final TreeService treeService;

    @GetMapping("/all/{id}")
    public List<TreeShortDto> getAllByForestId(@PathVariable Long id) {
        return treeService.getAllByForestId(id);
    }

    @PostMapping
    TreeShortDto create(@RequestBody TreeShortDto dto) {
        return treeService.create(dto);
    }

    @PostMapping("/{id}")
    TreeFullDto addIncrementation(@RequestBody IncrementationDto dto, @PathVariable Long id) {
        return treeService.addIncrementation(dto, id);
    }

    @GetMapping("/{id}")
    TreeFullDto getById(@PathVariable Long id) {
        return treeService.getById(id);
    }

    @PutMapping("/{id}")
    TreeShortDto update(@PathVariable Long id, @RequestBody TreeShortDto dto) {
        return treeService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        treeService.delete(id);
    }

}