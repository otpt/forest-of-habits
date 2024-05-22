package hh.forest_of_habits.controller;

import hh.forest_of_habits.dto.request.IncrementationRequest;
import hh.forest_of_habits.dto.request.TreeRequest;
import hh.forest_of_habits.dto.request.TreeStatus;
import hh.forest_of_habits.dto.response.TreeFullResponse;
import hh.forest_of_habits.dto.response.TreeIncrementsResponse;
import hh.forest_of_habits.dto.response.TreeResponse;
import hh.forest_of_habits.service.TreeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/tree")
public class TreeController {
    private final TreeService treeService;

    @GetMapping("/by_forest/{id}")
    public List<TreeResponse> getAllByForestId(@PathVariable Long id,
                                               @RequestParam(required = false, defaultValue = "ALL")
                                               TreeStatus status) {
        return treeService.getAllByForestId(id, status);
    }

    @PostMapping
    TreeResponse create(@RequestBody TreeRequest treeRequest) {
        return treeService.create(treeRequest);
    }

    @PostMapping("/{id}")
    TreeIncrementsResponse addIncrementation(@RequestBody IncrementationRequest incrementationRequest,
                                             @PathVariable Long id) {
        return treeService.addIncrementation(incrementationRequest, id);
    }

    @GetMapping("/{id}")
    TreeFullResponse getById(@PathVariable Long id) {
        return treeService.getById(id);
    }

    @PatchMapping("/{id}")
    TreeResponse update(@PathVariable Long id, @RequestBody TreeRequest treeRequest) {
        return treeService.update(id, treeRequest);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        treeService.delete(id);
    }

}