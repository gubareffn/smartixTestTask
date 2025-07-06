package app.smartix_test_task.repository;

import app.smartix_test_task.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Integer> {

    boolean existsById(Long id);

    @Query("SELECT p FROM Product p JOIN FETCH p.category WHERE p.id = :id")
    Product findById(@Param("id") Long id);

    @Query("SELECT p FROM Product p WHERE (:minPrice IS NULL OR p.price >= :minPrice) AND (:maxPrice IS NULL OR p.price <= :maxPrice)")
    Page<Product> sortByPrice(@Param("minPrice")BigDecimal minPrice,
                              @Param("maxPrice")BigDecimal maxPrice,
                              Pageable pageable);

    Page <Product> findAllByCategoryName(String categoryName, Pageable pageable);
}
