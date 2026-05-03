package gestion_inventarios.backend.infrastructure.in.rest.dashboard.dto;

public record DashboardStockByLocationResponse(
    Long locationId,
    String locationName,
    String locationCode,
    long totalStock
) {}
