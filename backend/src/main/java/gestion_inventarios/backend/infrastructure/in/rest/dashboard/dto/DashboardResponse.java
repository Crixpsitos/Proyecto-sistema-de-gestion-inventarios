package gestion_inventarios.backend.infrastructure.in.rest.dashboard.dto;

import java.util.List;

public record DashboardResponse(
    DashboardOverviewResponse overview,
    DashboardMovementMetricsResponse movementMetrics,
    List<DashboardTopProductResponse> topProducts,
    List<DashboardStockByLocationResponse> stockByLocation,
    List<DashboardAlertResponse> alerts
) {}
