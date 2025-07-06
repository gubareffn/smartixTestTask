package app.smartix_test_task.repository;

import app.smartix_test_task.models.Category;
import app.smartix_test_task.models.Product;
import app.smartix_test_task.models.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating, Integer> {
    Rating findById(Long id);
}
