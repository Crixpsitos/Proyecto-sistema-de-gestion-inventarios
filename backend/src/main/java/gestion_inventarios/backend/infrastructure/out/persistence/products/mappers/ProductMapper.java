package gestion_inventarios.backend.infrastructure.out.persistence.products.mappers;

import java.util.Collections;

import org.springframework.stereotype.Component;

import gestion_inventarios.backend.domain.model.category.Category;
import gestion_inventarios.backend.domain.model.products.Product;
import gestion_inventarios.backend.infrastructure.out.persistence.category.entity.CategoryEntity;
import gestion_inventarios.backend.infrastructure.out.persistence.products.entity.ProductEntity;
import gestion_inventarios.backend.infrastructure.out.persistence.products.entity.ProductPriceEmbeddable;

@Component
public class ProductMapper {

    public Product toDomain(ProductEntity e) {
        return new Product(
            e.getId(),
            e.getSku(),
            e.getName(),
            e.getDescription(),
            new Category(e.getCategory().getId(), e.getCategory().getName(), e.getCategory().getDescription(), e.getCategory().getCreatedAt(), e.getCategory().getUpdatedAt()),
            e.getBrand(),
            e.getModel(),
            e.getColor(),
            e.getSize(),
            e.getImage(),
            e.getQuantity(),
            new gestion_inventarios.backend.domain.model.products.ProductPrice(e.getProductPrice().getPrice(), java.util.Currency.getInstance(e.getProductPrice().getCurrency())),
            e.isActive(),
            e.getCreatedAt(),
            e.getUpdatedAt()
        );
    }
    
    public ProductEntity toEntity(Product product) {
        

        ProductPriceEmbeddable priceEmbeddable = new ProductPriceEmbeddable(
            product.getProductPrice().price(), 
            product.getProductPrice().currency().getCurrencyCode() 
        );
        
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(product.getId());
        productEntity.setSku(product.getSku());
        productEntity.setName(product.getName());
        productEntity.setDescription(product.getDescription());
   
        CategoryEntity categoryEntity = new CategoryEntity(
            product.getCategory().getId(),
            product.getCategory().getName(),
            product.getCategory().getDescription(),
            Collections.emptyList(),
            product.getCategory().getCreatedAt(),
            product.getCategory().getUpdatedAt()
        );
        
        productEntity.setCategory(categoryEntity);
        productEntity.setBrand(product.getBrand());
        productEntity.setModel(product.getModel());
        productEntity.setColor(product.getColor());
        productEntity.setSize(product.getSize());
        productEntity.setImage(product.getImage());
        productEntity.setQuantity(product.getQuantity());
        productEntity.setProductPrice(priceEmbeddable);
        productEntity.setActive(product.isActive());
        productEntity.setCreatedAt(product.getCreatedAt());
        productEntity.setUpdatedAt(product.getUpdatedAt());
        
        return productEntity;
    }
}