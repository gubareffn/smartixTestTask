package app.smartix_test_task.controllers;

import app.smartix_test_task.dto.ProductDto;
import app.smartix_test_task.models.Product;
import app.smartix_test_task.service.ProductServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductServiceImpl productServiceImpl;

    public ProductController(ProductServiceImpl productServiceImpl) {
        this.productServiceImpl = productServiceImpl;
    }

    @GetMapping
    public ResponseEntity<Page<Product>> getAllProducts(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(productServiceImpl.getAllProducts(PageRequest.of(page, size)));
    }

    @GetMapping("/price-filter")
    public ResponseEntity<Page<Product>> getAllProductsSortedByPrice(@RequestParam(required = false) BigDecimal minPrice,
                                                                     @RequestParam(required = false) BigDecimal maxPrice,
                                                                     @RequestParam(defaultValue = "0") int page,
                                                                     @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(productServiceImpl.getAllProductsByPriceRange(minPrice, maxPrice, PageRequest.of(page, size)));
    }

    @GetMapping("/category")
    public ResponseEntity<Page<Product>> getAllProductsByCategoryName(@RequestParam String categoryName,
                                                                      @RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(productServiceImpl.getAllProductsByCategory(categoryName, PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Integer id) {
        Product product = productServiceImpl.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer id) {
        productServiceImpl.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/create")
    public ResponseEntity<?> createProduct(@RequestBody ProductDto product) {
        productServiceImpl.saveProduct(product);
        return ResponseEntity.ok("Successfully created product");
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable Long productId, @RequestBody ProductDto product) {
        productServiceImpl.updateProduct(productId, product);
        return ResponseEntity.ok("Successfully updated product");
    }

    @PostMapping("/import")
    public ResponseEntity<String> importProducts() {
        productServiceImpl.importProducts();
        return ResponseEntity.ok("Successfully imported products");
    }
}
