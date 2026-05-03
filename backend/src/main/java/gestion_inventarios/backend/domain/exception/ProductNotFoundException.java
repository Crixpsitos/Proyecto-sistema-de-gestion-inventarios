package gestion_inventarios.backend.domain.exception;

import java.util.UUID;

public class ProductNotFoundException extends RuntimeException {
    

    public ProductNotFoundException(UUID id) {
        super("Product con id " + id + " no encontrado");
    }

    public ProductNotFoundException(String sku) {
        super("Product con sku " + sku + " no encontrado");
    }

}
