package app.smartix_test_task.service;

import app.smartix_test_task.dto.ProductDto;
import app.smartix_test_task.models.Category;
import app.smartix_test_task.models.Product;
import app.smartix_test_task.models.Rating;
import app.smartix_test_task.repository.CategoryRepository;
import app.smartix_test_task.repository.ProductRepository;
import app.smartix_test_task.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final RatingRepository ratingRepository;
    private final RestClient restClient = RestClient.builder()
            .baseUrl("https://fakestoreapi.com")
            .build();;

    @Override
    public void importProducts() {
        ProductDto[] products = restClient.get()
                .uri("/products")
                .retrieve()
                .body(ProductDto[].class);

        if (products != null) {
            Arrays.stream(products).forEach(this::saveProduct);
        }
    }

    @Override
    @Transactional
    public void saveProduct(ProductDto product) {
        Product savedProduct = new Product();

        savedProduct.setTitle(product.getTitle());
        savedProduct.setDescription(product.getDescription());
        savedProduct.setPrice(product.getPrice());
        savedProduct.setImage(product.getImage());

        Category existingCategory = categoryRepository.findByName(product.getCategory());
        if (existingCategory == null) {
            Category newCategory = new Category();
            newCategory.setName(product.getCategory());
            existingCategory = categoryRepository.save(newCategory);
        }
        savedProduct.setCategory(existingCategory);

        if (product.getRating() != null) {
            Rating rating = new Rating();
            rating.setRate(product.getRating().getRate());
            rating.setCount(product.getRating().getCount());
            savedProduct.setRating(rating);
            ratingRepository.save(rating);
        }
        productRepository.save(savedProduct);
    }

    @Override
    public void updateProduct(Long id, ProductDto updatedProduct) {
        Product existingProduct = productRepository.findById(id);
        if (existingProduct == null) {
            throw new RuntimeException("Product not found with id: " + id);
        }

        existingProduct.setTitle(updatedProduct.getTitle());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setImage(updatedProduct.getImage());

        if (!existingProduct.getCategory().getName().equals(updatedProduct.getCategory())) {
            Category category = categoryRepository.findByName(updatedProduct.getCategory());
            if (category == null) {
                Category newCategory = new Category();
                newCategory.setName(updatedProduct.getCategory());
                category = categoryRepository.save(newCategory);
            }
            existingProduct.setCategory(category);
        }

        if (updatedProduct.getRating() != null) {
            if (existingProduct.getRating() == null) {
                existingProduct.setRating(new Rating());
            }
            existingProduct.getRating().setRate(updatedProduct.getRating().getRate());
            existingProduct.getRating().setCount(updatedProduct.getRating().getCount());
        }
    }

    @Override
    public Product getProductById(Integer id) {
        return productRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Product not found with ID: " + id));
    }

    @Override
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Page<Product> getAllProductsByCategory(String categoryName, Pageable pageable) {
        return productRepository.findAllByCategoryName(categoryName, pageable);
    }

    @Override
    public Page<Product> getAllProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        return productRepository.sortByPrice(minPrice, maxPrice, pageable);
    }

    @Override
    public void deleteById(Integer id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else throw new RuntimeException("Product not found with id: " + id);
    }
}
