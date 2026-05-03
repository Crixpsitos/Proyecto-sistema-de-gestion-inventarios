package gestion_inventarios.backend.infrastructure.in.rest.product.dto;

public record ProductExistResponse(boolean exist) {
    public static ProductExistResponse from(boolean exist) {
        return new ProductExistResponse(exist);
    }
}
