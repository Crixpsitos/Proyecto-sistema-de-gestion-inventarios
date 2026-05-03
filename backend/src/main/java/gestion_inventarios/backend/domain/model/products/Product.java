package gestion_inventarios.backend.domain.model.products;

import java.time.LocalDateTime;
import java.util.UUID;

import gestion_inventarios.backend.domain.model.category.Category;
import lombok.Getter;

@Getter
public class Product {
    
    private final UUID id;
    private String sku;
    private String name;
    private String description;
    private Category category;
    private String brand; 
    private String model;
    private String color;
    private String size;
    private String image;
    private int quantity;
    private ProductPrice productPrice;
    private boolean active;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Product(UUID id, String sku, String name, String description, Category category, String brand, String model, String color, String size, String image, int quantity, ProductPrice productPrice, boolean active, LocalDateTime createdAt, LocalDateTime updatedAt) {
        if (sku == null || sku.isBlank()) throw new IllegalArgumentException("El SKU es obligatorio");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("El nombre es obligatorio");
        if (productPrice == null) throw new IllegalArgumentException("El precio no puede ser nulo");
        if (quantity < 0) throw new IllegalArgumentException("La cantidad de stock no puede ser negativa");

        this.id = id;
        this.sku = sku;
        this.name = name;
        this.description = description;
        this.category = category;
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.size = size;
        this.image = image;
        this.quantity = quantity;
        this.productPrice = productPrice;
        this.active = active;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
        this.updatedAt = updatedAt != null ? updatedAt : this.createdAt;
    }

    public void updatePrice(ProductPrice productPrice) {
        if (productPrice == null) {
            throw new IllegalArgumentException("El nuevo precio no puede ser nulo");
        }
        this.productPrice = productPrice;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("La cantidad de stock no puede ser negativa");
        }
        this.quantity = quantity;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateActive(boolean active) {
        this.active = active;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateImage(String image) {
        this.image = image;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateBrand(String brand) {
        if (brand == null || brand.isBlank()) {
            throw new IllegalArgumentException("La marca no puede estar vacía");
        }
        this.brand = brand;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("El nombre del producto no puede quedar vacío");
        }
        this.name = name;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateDescription(String description) {
        if (description != null && description.length() > 500) {
            throw new IllegalArgumentException("La descripción es demasiado larga");
        }
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("El producto debe pertenecer a una categoría");
        }
        this.category = category;
        this.updatedAt = LocalDateTime.now();
    }

        public void updateModel(String model) {
            if (model == null || model.isBlank()) {
                throw new IllegalArgumentException("El modelo no puede estar vacío");
            }
            this.model = model;
            this.updatedAt = LocalDateTime.now();
        }

        public void updateColor(String color) {
            if (color == null || color.isBlank()) {
                throw new IllegalArgumentException("El color no puede estar vacío");
            }
            this.color = color;
            this.updatedAt = LocalDateTime.now();
        }

        public void updateSize(String size) {
            if (size == null || size.isBlank()) {
                throw new IllegalArgumentException("El tamaño no puede estar vacío");
            }
            this.size = size;
            this.updatedAt = LocalDateTime.now();
        }

        public void updateSku(String sku2) {
            if (sku2 == null || sku2.isBlank()) {
                throw new IllegalArgumentException("El SKU no puede estar vacío");
            }
            this.sku = sku2;
            this.updatedAt = LocalDateTime.now();
        }
}