package gestion_inventarios.backend.infrastructure.out.persistence.products.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductPriceEmbeddable {
    @Column(name = "price_amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal price;


    @Column(name = "price_currency", nullable = false, length = 3)
    private String currency;
}
