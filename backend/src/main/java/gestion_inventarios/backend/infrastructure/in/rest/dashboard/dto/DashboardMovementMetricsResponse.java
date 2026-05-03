package gestion_inventarios.backend.infrastructure.in.rest.dashboard.dto;

public record DashboardMovementMetricsResponse(
    long today,
    long last7Days,
    long last7DaysIn,
    long last7DaysOut,
    long last7DaysTransfer
) {}
