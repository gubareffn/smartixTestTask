package app.smartix_test_task.controllers;

import app.smartix_test_task.models.Category;
import app.smartix_test_task.service.impl.CategoryServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
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

    @Tag(name = "Получения списка уникальных категорий товаров", description = "Возвращает список категорий товаров без дубликатов")
    @GetMapping("/unique")
    public ResponseEntity<List<Category>> findAllUniqueCategories() {
        return new ResponseEntity<>(categoryService.findUniqueCategories(), HttpStatus.OK);
    }
}
