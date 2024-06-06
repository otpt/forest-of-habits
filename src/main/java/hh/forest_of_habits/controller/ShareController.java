package hh.forest_of_habits.controller;

import hh.forest_of_habits.dto.request.TreeStatus;
import hh.forest_of_habits.dto.response.ForestResponse;
import hh.forest_of_habits.dto.response.TreeFullResponse;
import hh.forest_of_habits.dto.response.TreeResponse;
import hh.forest_of_habits.service.ForestService;
import hh.forest_of_habits.service.TreeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/shared")
public class ShareController {
    private final ForestService forestService;
    private final TreeService treeService;

    @GetMapping("/{id}")
    ForestResponse getForest(@PathVariable UUID id) {
        return forestService.getByUuid(id);
    }

    @GetMapping("/tree/{id}")
    TreeFullResponse getTree(@PathVariable Long id) {
        return treeService.getById(id);
    }

    @GetMapping("/by_forest/{id}")
    public List<TreeResponse> getAllByForestId(@PathVariable UUID id,
            @RequestParam(required = false, defaultValue = "ALL") TreeStatus status) {
        return treeService.getAllByForestUuid(id, status);
    }
}
