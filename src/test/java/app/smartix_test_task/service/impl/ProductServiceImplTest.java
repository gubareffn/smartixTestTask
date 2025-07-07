package app.smartix_test_task.service.impl;

import app.smartix_test_task.dto.ProductDto;
import app.smartix_test_task.models.Category;
import app.smartix_test_task.models.Product;
import app.smartix_test_task.models.Rating;
import app.smartix_test_task.repository.CategoryRepository;
import app.smartix_test_task.repository.ProductRepository;
import app.smartix_test_task.repository.RatingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private RatingRepository ratingRepository;

    @InjectMocks
    private ProductServiceImpl productServiceImpl;

    private List<Product> testProductList;

    @BeforeEach
    void setUp() {
        testProductList = getProduct();
    }

    private List<Product> getProduct() {
        List<Product> testProductList = new ArrayList<>();

        Category firstCategory = new Category();
        firstCategory.setId(1L);
        firstCategory.setName("Electronics");

        Rating rating = new Rating();
        rating.setId(1L);
        rating.setRate(BigDecimal.valueOf(4.5));
        rating.setCount(100);

        Product firstProduct = new Product();
        firstProduct.setId(1L);
        firstProduct.setTitle("Test Product1");
        firstProduct.setDescription("Test Description1");
        firstProduct.setPrice(BigDecimal.valueOf(99.99));
        firstProduct.setImage("test.jpg");
        firstProduct.setCategory(firstCategory);
        firstProduct.setRating(rating);
        testProductList.add(firstProduct);

        Category secondCategory = new Category();
        secondCategory.setId(1L);
        secondCategory.setName("men's clothing");

        Product secondProduct = new Product();
        secondProduct.setId(2L);
        secondProduct.setTitle("Test Product2");
        secondProduct.setDescription("Test Description2");
        secondProduct.setPrice(BigDecimal.valueOf(170));
        secondProduct.setImage("test.jpg");
        secondProduct.setCategory(secondCategory);
        secondProduct.setRating(rating);
        testProductList.add(secondProduct);

        return testProductList;
    }

    @Test
    @DisplayName("saveProduct_ShouldCreateNewProduct_WhenIdIsNull - Создание нового продукта (id == null)")
    void saveProduct_ShouldCreateNewProduct_WhenIdIsNull() {
        ProductDto dto = new ProductDto();
        dto.setTitle("New Product");
        dto.setCategory("New Category");
        Rating rating = new Rating();
        rating.setRate(BigDecimal.valueOf(4.5));
        rating.setCount(100);
        dto.setRating(rating);

        when(categoryRepository.save(any(Category.class))).thenAnswer(inv -> inv.getArgument(0));
        when(ratingRepository.save(any(Rating.class))).thenAnswer(inv -> inv.getArgument(0));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        productServiceImpl.saveProduct(dto);

        verify(categoryRepository).save(any(Category.class));
        verify(ratingRepository).save(any(Rating.class));
        verify(productRepository).save(any(Product.class));
    }

    @Test
    @DisplayName("GetProductByIdWhenProductExists - получение существующего товара по его айди")
    void testGetProductByIdWhenProductExists() {
        Integer productId = 1;
        Product expectedProduct = testProductList.getFirst();

        when(productRepository.findById(productId)).thenReturn(Optional.of(expectedProduct));

        Product resultProduct = productServiceImpl.getProductById(productId);

        assertNotNull(resultProduct);
        assertEquals(expectedProduct.getId(), resultProduct.getId());
    }

    @Test
    @DisplayName("testGetProductById_WhenProductDoesNotExist_ShouldThrowException - получение несуществующего товара")
    void testGetProductById_ShouldReturnCorrectProduct() {
        Integer productId = 100;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            productServiceImpl.getProductById(productId);
        });
    }

    @Test
    @DisplayName("testGetAllProducts_ShouldReturnCorrectProductList - получение списка всех товаров")
    void testGetAllProducts_ShouldReturnCorrectProductList() {
        Pageable pageable = PageRequest.of(0, 10);
        when(productRepository.findAll(pageable)).thenReturn(new PageImpl<> (testProductList));

        Page<Product> resultList = productServiceImpl.getAllProducts(pageable);

        assertEquals(testProductList.size(), resultList.getTotalElements());
    }

    @Test
    @DisplayName("testGetAllProductsByCategory_ShouldFilterByCategoryName - получение списка всех товаров по категории")
    void testGetAllProductsByCategory_ShouldFilterByCategoryName() {
        Pageable pageable = PageRequest.of(0, 10);
        String categoryName = "Electronics";
        when(productRepository.findAllByCategoryName(categoryName, pageable))
                .thenReturn(new PageImpl<>(List.of(testProductList.get(0))));

        Page<Product> result = productServiceImpl.getAllProductsByCategory(categoryName, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(categoryName, result.getContent().get(0).getCategory().getName());
    }

    @Test
    @DisplayName("testGetAllProductsSorted_ShouldReturnSortedByAscendingPrice - проверка сортировки по возрастанию цены")
    void testGetAllProductsSorted_ShouldReturnSortedByAscendingPrice() {
        Sort sort = Sort.by(Sort.Order.asc("price"));
        Pageable pageable = PageRequest.of(0, 10, sort);
        BigDecimal expectedPrice = BigDecimal.valueOf(99.99);

        when(productRepository.findAll(pageable)).thenReturn(new PageImpl<> (List.of(testProductList.get(0))));
        Page<Product> result = productServiceImpl.getAllProductsSorted(pageable);

        assertEquals(expectedPrice, result.getContent().get(0).getPrice());
    }

    @Test
    @DisplayName("testGetAllProductsSorted_ShouldReturnSortedByDescendingPrice - проверка сортировки по убыванию цены")
    void testGetAllProductsSorted_ShouldReturnSortedByDescendingPrice() {
        Sort sort = Sort.by(Sort.Order.desc("price"));
        Pageable pageable = PageRequest.of(0, 10, sort);
        BigDecimal expectedPrice = BigDecimal.valueOf(170);

        when(productRepository.findAll(pageable)).thenReturn(new PageImpl<> (List.of(testProductList.get(1))));
        Page<Product> result = productServiceImpl.getAllProductsSorted(pageable);

        assertEquals(expectedPrice, result.getContent().getFirst().getPrice());
    }

    @Test
    @DisplayName("getAllProductsSorted_ShouldHandleMultipleSortFields - проверка сортировки по нескольким полям")
    void testGetAllProductsSorted_ShouldHandleMultipleSortFields() {
        Sort sort = Sort.by(
                Sort.Order.asc("price"),
                Sort.Order.desc("category.name")
        );
        Pageable pageable = PageRequest.of(0, 10, sort);
        BigDecimal expectedPrice = BigDecimal.valueOf(99.99);

        when(productRepository.findAll(pageable)).thenReturn(new PageImpl<>(testProductList));
        Page<Product> result = productServiceImpl.getAllProductsSorted(pageable);

        assertEquals(expectedPrice, result.getContent().get(0).getPrice());
    }

    @Test
    @DisplayName("getAllProductsByPriceRange_WithPriceRange_ReturnsFilteredProducts - получения списка товаров в ценовом диапазоне")
    void getAllProductsByPriceRange_WithPriceRange_ReturnsFilteredProducts() {
        Pageable pageable = PageRequest.of(0, 10);
        BigDecimal minPrice = BigDecimal.valueOf(50);
        BigDecimal maxPrice = BigDecimal.valueOf(150);

        when(productRepository.sortByPrice(minPrice, maxPrice, pageable))
                .thenReturn(new PageImpl<> (List.of(testProductList.get(0))));

        Page<Product> result = productServiceImpl.getAllProductsByPriceRange(minPrice, maxPrice, pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    @DisplayName("testGetAllProductsByPriceRange_NoPriceRange_ShouldReturnAllProducts - все параметры null")
    void testGetAllProductsByPriceRange_NoPriceRange_ShouldReturnAllProducts() {
        Pageable pageable = PageRequest.of(0, 10);
        BigDecimal minPrice = null;
        BigDecimal maxPrice = null;
        when(productRepository.sortByPrice(minPrice, maxPrice, pageable))
                .thenReturn(new PageImpl<>(testProductList));

        Page<Product> result = productServiceImpl.getAllProductsByPriceRange(minPrice, maxPrice, pageable);

        assertEquals(2, result.getTotalElements());
    }

    @Test
    @DisplayName("deleteById_ShouldCallRepositoryDelete - Проверка удаления для продукта по айди")
    void deleteById_ShouldCallRepositoryDelete() {
        Integer productId = 1;
        when(productRepository.existsById(productId)).thenReturn(true);
        doNothing().when(productRepository).deleteById(productId);

        productServiceImpl.deleteById(productId);

        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    @DisplayName("deleteById_WhenProductNotFound_ShouldThrowException - Проверка удаления для несуществующего продукта")
    void deleteById_ShouldThrowExceptionWhenProductNotFound() {
        Integer productId = 100;
        when(productRepository.existsById(productId)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> {
            productServiceImpl.deleteById(productId);
        });
    }
}