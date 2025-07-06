package app.smartix_test_task.dto;

import app.smartix_test_task.models.Product;
import app.smartix_test_task.models.Rating;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Сущность товара")
public class ProductDto {
    private Long id;

    @Schema(description = "Наименование товара", example = "Mens Casual Premium Slim Fit T-Shirts")
    private String title;

    @Schema(description = "Цена товара", example = "22.3")
    private BigDecimal price;

    @Schema(description = "Url-адрес изображения", example = "https://fakestoreapi.com/img/71-3HjGNDUL._AC_SY879._SX._UX._SY._UY_.jpg")
    private String image;

    @Schema(description = "Описание товара", example = "Slim-fitting style, contrast raglan long sleeve, three-button henley placket, light weight...")
    private String description;

    @Schema(description = "Категория товара", example = "men's clothing")
    private String category;

    @Schema(description = "Количество и средняя оценка товара")
    private Rating rating;


    public ProductDto(Product product) {
        this.title = product.getTitle();
        this.price = product.getPrice();
        this.image = product.getImage();
        this.description = product.getDescription();
        this.category = product.getCategory().getName();
        this.rating = product.getRating();
    }
}
