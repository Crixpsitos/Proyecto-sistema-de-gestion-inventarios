package gestion_inventarios.backend.infrastructure.in.rest.dashboard.dto;

public record DashboardTopProductResponse(
    String productId,
    String name,
    String sku,
    long totalQuantity,
    long totalMovements
) {}
