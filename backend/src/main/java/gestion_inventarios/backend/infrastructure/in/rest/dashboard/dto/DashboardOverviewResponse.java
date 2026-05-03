package gestion_inventarios.backend.infrastructure.in.rest.dashboard.dto;

import java.time.LocalDateTime;

public record DashboardOverviewResponse(
    long totalProducts,
    long totalCategories,
    long totalLocations,
    long totalStock,
    long totalMovements,
    LocalDateTime lastMovementAt
) {}
