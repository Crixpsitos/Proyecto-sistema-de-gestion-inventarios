package gestion_inventarios.backend.infrastructure.in.rest.product.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import gestion_inventarios.backend.domain.model.category.Category;
import gestion_inventarios.backend.domain.model.products.Product;


public record ProductResponse(
    UUID id,
    String sku,
    String name,
    String description,
    String image,
    int quantity,
    BigDecimal price,
    String currency,
    Category category,
    String model,
    String brand,
    String color,
    String size,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    
  
    public static ProductResponse from(Product product) {
        return new ProductResponse(
            product.getId(),
            product.getSku(),
            product.getName(),
            product.getDescription(),
            product.getImage(),
            product.getQuantity(),
            product.getProductPrice().price(),      
            product.getProductPrice().currency().getCurrencyCode(),
            product.getCategory(),
            product.getModel(),
            product.getBrand(),
            product.getColor(),
            product.getSize(),
            product.getCreatedAt(),
            product.getUpdatedAt()
        );
    }
}