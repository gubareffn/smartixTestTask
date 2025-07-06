package app.smartix_test_task.dto;

import app.smartix_test_task.models.Product;
import app.smartix_test_task.models.Rating;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ProductDto {
    private Long id;
    private String title;
    private BigDecimal price;
    private String image;
    private String description;
    private String category;
    private Rating rating;
    private BigDecimal rate;
    private Integer count;


    public ProductDto(Product product) {
        this.id = product.getId();
        this.title = product.getTitle();
        this.price = product.getPrice();
        this.image = product.getImage();
        this.description = product.getDescription();
        this.category = product.getCategory().getName();
        this.rating = product.getRating();
        this.rate = product.getRating().getRate();
        this.count = product.getRating().getCount();
    }
}
