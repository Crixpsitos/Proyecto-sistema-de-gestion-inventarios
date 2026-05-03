package gestion_inventarios.backend.domain.model.products;

import java.math.BigDecimal;

import java.util.Currency;

public record ProductPrice(BigDecimal price, Currency currency) {
    
    public ProductPrice {
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio debe ser mayor o igual a cero");
        }
        if (currency == null) {
            throw new IllegalArgumentException("La moneda es obligatoria");
        }
    }

   

}

 
