package app.smartix_test_task.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "product")
public class Product {
    /**
     * Идентификатор товара
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", nullable = false)
    private Long id;

    /**
     * Наименование товара
     */
    @Column(name = "title", nullable = false)
    private String title;

    /**
     * Стоимость товара
     */
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    /**
     * Изображение товара
     */
    @Column(name = "image")
    private String image;

    /**
     * Описание товара
     */
    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;


    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToOne
    @JoinColumn(name = "rating_id")
    private Rating rating;

}