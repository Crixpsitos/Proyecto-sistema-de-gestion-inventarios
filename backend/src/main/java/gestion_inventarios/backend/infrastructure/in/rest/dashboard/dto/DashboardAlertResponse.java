package gestion_inventarios.backend.infrastructure.in.rest.dashboard.dto;

public record DashboardAlertResponse(
    String productId,
    String name,
    String sku,
    long totalStock,
    int threshold
) {}
