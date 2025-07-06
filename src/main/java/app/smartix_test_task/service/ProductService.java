package app.smartix_test_task.service;

import app.smartix_test_task.dto.ProductDto;
import app.smartix_test_task.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

public interface ProductService {
    void deleteById(Integer id);

    Product getProductById(Integer id);

    void saveProduct(ProductDto product);

    void updateProduct(Long id, ProductDto productUpdate);

    void importProducts();

    Page<Product> getAllProducts(Pageable pageable);

    Page<Product> getAllProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    Page<Product> getAllProductsByCategory(String categoryName, Pageable pageable);
}
