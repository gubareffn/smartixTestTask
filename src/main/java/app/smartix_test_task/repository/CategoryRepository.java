package app.smartix_test_task.repository;

import app.smartix_test_task.models.Category;
import app.smartix_test_task.models.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Category findByName(String name);

    @Query("SELECT DISTINCT p.category FROM Product p ")
    List<Category> findAllUniqueCategories();

    Rating findById(Long id);
}
