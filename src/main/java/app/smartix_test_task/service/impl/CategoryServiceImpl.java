package app.smartix_test_task.service.impl;

import app.smartix_test_task.models.Category;
import app.smartix_test_task.repository.CategoryRepository;
import app.smartix_test_task.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Получения списка всех уникальных категорий
     */
    @Override
    public List<Category> findUniqueCategories() {
        return categoryRepository.findAllUniqueCategories();
    }
}
