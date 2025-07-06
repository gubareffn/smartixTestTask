package app.smartix_test_task.controllers;

import app.smartix_test_task.models.Category;
import app.smartix_test_task.service.CategoryServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    private final CategoryServiceImpl categoryService;

    public CategoryController(CategoryServiceImpl categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/unique")
    public ResponseEntity<List<Category>> findAllUniqueCategories() {
        return ResponseEntity.ok(categoryService.findUniqueCategories());
    }
}
