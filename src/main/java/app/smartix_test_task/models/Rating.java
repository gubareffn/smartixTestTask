package app.smartix_test_task.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "rating")
public class Rating {
    /**
     * Идентификатор товара
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rating_id", nullable = false)
    private Long id;

    /**
     * Количество товара
     */
    @Column(name = "count", nullable = false)
    private Integer count;

    /**
     * Средняя оценка товара
     */
    @Column(name = "rate", nullable = false)
    private BigDecimal rate;

    @JsonIgnore
    @OneToOne(mappedBy = "rating")
    private Product product;

}