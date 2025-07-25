package app.smartix_test_task.service;

import app.smartix_test_task.models.Category;

import java.util.List;

public interface CategoryService {
    /**
     * Получения списка всех уникальных категорий
     */
    List<Category> findUniqueCategories();
}
