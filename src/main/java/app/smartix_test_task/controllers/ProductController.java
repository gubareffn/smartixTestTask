package app.smartix_test_task.controllers;

import app.smartix_test_task.dto.ProductDto;
import app.smartix_test_task.models.Product;
import app.smartix_test_task.service.impl.ProductServiceImpl;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
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

    @Tag(name = "Получение всех товаров",
            description = "Возвращает список товаров с поддержкой пагинации")
    @GetMapping
    public ResponseEntity<Page<Product>> getAllProducts(@RequestParam(defaultValue = "0") @Parameter(description = "Номер страницы") int page,
                                                        @RequestParam(defaultValue = "10") @Parameter(description = "Число элементов") int size) {
        return new ResponseEntity<>(productServiceImpl.getAllProducts(PageRequest.of(page, size)), HttpStatus.OK);
    }

    @Tag(name = "Фильтрация товаров по стоимости",
            description = "Возвращает список товаров со стоимостью в указанном диапазоне")
    @GetMapping("/price-filter")
    public ResponseEntity<Page<Product>> getAllProductsSortedByPrice(@RequestParam @Parameter(description = "Нижняя граница цены") BigDecimal minPrice,
                                                                     @RequestParam @Parameter(description = "Верхняя граница цены") BigDecimal maxPrice,
                                                                     @RequestParam(defaultValue = "0") @Parameter(description = "Номер страницы") int page,
                                                                     @RequestParam(defaultValue = "10") @Parameter(description = "Число элементов") int size) {
        return new ResponseEntity<>(productServiceImpl.getAllProductsByPriceRange(minPrice, maxPrice, PageRequest.of(page, size)), HttpStatus.OK);
    }

    @Tag(name = "Получение товаров по категории",
            description = "Возвращает список всех товаров с указанной категорией")
    @GetMapping("/category")
    public ResponseEntity<Page<Product>> getAllProductsByCategoryName(@RequestParam @Parameter(description = "Название категории") String categoryName,
                                                                      @RequestParam(defaultValue = "0") @Parameter(description = "Номер страницы") int page,
                                                                      @RequestParam(defaultValue = "10") @Parameter(description = "Число элементов")int size) {
        return new ResponseEntity<>(productServiceImpl.getAllProductsByCategory(categoryName, PageRequest.of(page, size)), HttpStatus.OK);
    }

    @Tag(name = "Получение товара по ID ",
            description = "Возвращает товар с указанным идентификатором")
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Integer id) {
        Product product = productServiceImpl.getProductById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @Tag(name = "Удаление товара ",
            description = "Удаляет товар с указанным идентификатором")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer id) {
        productServiceImpl.deleteById(id);
        return new ResponseEntity<>("Successfully deleted product", HttpStatus.OK);
    }

    @Tag(name = "Создание нового товара ",
            description = "Создаёт новый товар")
    @PostMapping("/create")
    public ResponseEntity<?> createProduct(@RequestBody ProductDto product) {
        productServiceImpl.saveProduct(product);
        return new ResponseEntity<>("Successfully created product", HttpStatus.OK);
    }

    @Tag(name = "Редактирование существующего товара",
            description = "Редактирует товар с указанным идентификатором, если он существует")
    @PutMapping("/update")
    public ResponseEntity<?> updateProduct(@RequestBody ProductDto product) {
        productServiceImpl.saveProduct(product);
        return new ResponseEntity<>("Successfully updated product", HttpStatus.OK);
    }

    @Tag(name = "Импорт данных с внешнего апи",
            description = "Добавляет данные с внешнего апи (https://fakestoreapi.com/products) в базу данных")
    @PostMapping("/import")
    public ResponseEntity<String> importProducts() {
        productServiceImpl.importProducts();
        return new ResponseEntity<>("Successfully imported products", HttpStatus.OK);
    }
}
