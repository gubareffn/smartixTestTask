package app.smartix_test_task.service;

import app.smartix_test_task.dto.ProductDto;
import app.smartix_test_task.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

public interface ProductService {

    /**
     * Удаление товара по идентификатора
     * @param id идентификатор товара для удаления
     * @throws RuntimeException если товар не найден
     */
    void deleteById(Integer id);

    /**
     * Получение товара по ID
     * @param id идентификатор товара
     * @throws RuntimeException если товар не найден
     */
    Product getProductById(Integer id);

    /**
     * Создание нового товара
     * @param product dto товара с измененными полями
     */
    void saveProduct(ProductDto product);

    /**
     * Импорт товаров с внешнего апи
     */
    void importProducts();

    /**
     * Получения списка всех товаров с поддержкой пагинации
     */
    Page<Product> getAllProducts(Pageable pageable);

    /**
     * Получения товаров в указанном ценовом диапазоне с поддержкой пагинации
     * @param minPrice нижняя граница цены
     * @param maxPrice верхняя граница цены
     */
    Page<Product> getAllProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    /**
     * Получения товаров в указанном ценовом диапазоне с поддержкой пагинации
     * @param categoryName наименование категории
     */
    Page<Product> getAllProductsByCategory(String categoryName, Pageable pageable);

    /**
     * Получения списка товаров отсортированных по цене или категории в заданном направлении с поддержкой пагинации
     * @param sort параметры сортировки и пагинации
     */
    Page<Product> getAllProductsSorted(Pageable sort);
}
