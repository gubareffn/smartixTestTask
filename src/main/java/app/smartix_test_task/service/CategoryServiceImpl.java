package app.smartix_test_task.service;

import app.smartix_test_task.models.Category;
import app.smartix_test_task.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> findUniqueCategories() {
        return categoryRepository.findAllUniqueCategories();
    }
}
