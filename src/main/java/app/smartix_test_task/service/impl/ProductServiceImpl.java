package app.smartix_test_task.service.impl;

import app.smartix_test_task.dto.ProductDto;
import app.smartix_test_task.models.Category;
import app.smartix_test_task.models.Product;
import app.smartix_test_task.models.Rating;
import app.smartix_test_task.repository.CategoryRepository;
import app.smartix_test_task.repository.ProductRepository;
import app.smartix_test_task.repository.RatingRepository;
import app.smartix_test_task.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final RatingRepository ratingRepository;
    private final RestClient restClient = RestClient.builder()
            .baseUrl("https://fakestoreapi.com")
            .build();;

    /**
     * Импорт товаров с внешнего апи. При повторном импорте данные обновляются
     * Реализован планировщик. Каждые 30 минут данные синхронизируются с внешним апи
     */
    @Override
    @Scheduled(fixedRate = 30 * 60 * 1000)
    public void importProducts() {
        ProductDto[] products = restClient.get()
                .uri("/products")
                .retrieve()
                .body(ProductDto[].class);

        if (products != null) {
            Arrays.stream(products).forEach(this::saveProduct);
        }
    }

    /**
     * Создание нового товара либо редактирование по заданному id
     * @param productDto dto товара с измененными полями
     */
    @Override
    public void saveProduct(ProductDto productDto) {
        Product savedProduct;
        if (productDto.getId() != null && productDto.getId() != 0) {
            savedProduct = productRepository.findByTitle(productDto.getTitle());
            if (savedProduct == null) {
                savedProduct = new Product();
            }
        } else {
            savedProduct = new Product();
        }

        savedProduct.setTitle(productDto.getTitle());
        savedProduct.setDescription(productDto.getDescription());
        savedProduct.setPrice(productDto.getPrice());
        savedProduct.setImage(productDto.getImage());

        // Создание новой категории если такой нет в бд
        Category existingCategory = categoryRepository.findByName(productDto.getCategory());
        if (existingCategory == null) {
            Category newCategory = new Category();
            newCategory.setName(productDto.getCategory());
            existingCategory = categoryRepository.save(newCategory);
        }
        savedProduct.setCategory(existingCategory);

        // Обновление или создание рейтинга
        if (productDto.getRating() != null) {
            Rating rating;
            if (savedProduct.getRating() != null)
                 rating = savedProduct.getRating();
            else rating = new Rating();
            rating.setRate(productDto.getRating().getRate());
            rating.setCount(productDto.getRating().getCount());
            savedProduct.setRating(rating);
            ratingRepository.save(rating);
        }
        productRepository.save(savedProduct);
    }

    /**
     * Получение товара по ID
     * @param id идентификатор товара
     * @throws RuntimeException если товар не найден
     */
    @Override
    public Product getProductById(Integer id) {
        return productRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Product not found with ID: " + id));
    }

    /**
     * Получения списка всех товаров с поддержкой пагинации
     */
    @Override
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    /**
     * Получения товаров в указанном ценовом диапазоне с поддержкой пагинации
     * @param categoryName наименование категории
     */
    @Override
    public Page<Product> getAllProductsByCategory(String categoryName, Pageable pageable) {
        return productRepository.findAllByCategoryName(categoryName, pageable);
    }

    /**
     * Получения товаров в указанном ценовом диапазоне с поддержкой пагинации
     * @param minPrice нижняя граница цены
     * @param maxPrice верхняя граница цены
     */
    @Override
    public Page<Product> getAllProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        return productRepository.sortByPrice(minPrice, maxPrice, pageable);
    }

    /**
     * Получения списка товаров отсортированных по цене или категории в заданном направлении с поддержкой пагинации
     * @param sort параметры сортировки и пагинации
     */
    @Override
    public Page<Product> getAllProductsSorted(Pageable sort) {
        return productRepository.findAll(sort);
    }

    /**
     * Удаление товара по идентификатору
     * @param id идентификатор товара для удаления
     * @throws RuntimeException если товар не найден
     */
    @Override
    public void deleteById(Integer id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else throw new RuntimeException("Product not found with id: " + id);
    }
}
