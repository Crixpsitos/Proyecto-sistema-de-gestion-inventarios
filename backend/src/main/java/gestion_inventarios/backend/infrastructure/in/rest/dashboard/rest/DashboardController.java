package gestion_inventarios.backend.infrastructure.in.rest.dashboard.rest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gestion_inventarios.backend.domain.model.movements.MovementType;
import gestion_inventarios.backend.infrastructure.in.rest.dashboard.dto.DashboardAlertResponse;
import gestion_inventarios.backend.infrastructure.in.rest.dashboard.dto.DashboardMovementMetricsResponse;
import gestion_inventarios.backend.infrastructure.in.rest.dashboard.dto.DashboardOverviewResponse;
import gestion_inventarios.backend.infrastructure.in.rest.dashboard.dto.DashboardResponse;
import gestion_inventarios.backend.infrastructure.in.rest.dashboard.dto.DashboardStockByLocationResponse;
import gestion_inventarios.backend.infrastructure.in.rest.dashboard.dto.DashboardTopProductResponse;
import gestion_inventarios.backend.infrastructure.out.persistence.category.repository.CategoryJpaRepository;
import gestion_inventarios.backend.infrastructure.out.persistence.inventory.repository.InventoryJpaRepository;
import gestion_inventarios.backend.infrastructure.out.persistence.location.repository.LocationJpaRepository;
import gestion_inventarios.backend.infrastructure.out.persistence.movements.repository.MovementJpaRepository;
import gestion_inventarios.backend.infrastructure.out.persistence.products.repository.ProductJpaRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private static final int DEFAULT_LOW_STOCK_THRESHOLD = 10;

    private final ProductJpaRepository productJpaRepository;
    private final CategoryJpaRepository categoryJpaRepository;
    private final LocationJpaRepository locationJpaRepository;
    private final InventoryJpaRepository inventoryJpaRepository;
    private final MovementJpaRepository movementJpaRepository;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','EMPLOYEE')")
    public ResponseEntity<DashboardResponse> getDashboard(
        @RequestParam(defaultValue = "5") int topProductsLimit,
        @RequestParam(defaultValue = "10") int lowStockThreshold
    ) {
        long totalProducts = productJpaRepository.count();
        long totalCategories = categoryJpaRepository.count();
        long totalLocations = locationJpaRepository.count();
        long totalStock = safeLong(inventoryJpaRepository.getTotalStock());
        long totalMovements = movementJpaRepository.count();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dayStart = LocalDate.now().atStartOfDay();
        LocalDateTime dayEnd = LocalDate.now().atTime(LocalTime.MAX);
        LocalDateTime last7DaysStart = now.minusDays(7);

        DashboardOverviewResponse overview = new DashboardOverviewResponse(
            totalProducts,
            totalCategories,
            totalLocations,
            totalStock,
            totalMovements,
            movementJpaRepository.findLastMovementAt()
        );

        DashboardMovementMetricsResponse movementMetrics = new DashboardMovementMetricsResponse(
            safeLong(movementJpaRepository.countByCreatedAtBetween(dayStart, dayEnd)),
            safeLong(movementJpaRepository.countByCreatedAtBetween(last7DaysStart, now)),
            safeLong(movementJpaRepository.countByTypeAndCreatedAtBetween(MovementType.IN, last7DaysStart, now)),
            safeLong(movementJpaRepository.countByTypeAndCreatedAtBetween(MovementType.OUT, last7DaysStart, now)),
            safeLong(movementJpaRepository.countByTypeAndCreatedAtBetween(MovementType.TRANSFER, last7DaysStart, now))
        );

        int safeTopLimit = Math.max(1, Math.min(topProductsLimit, 20));
        List<DashboardTopProductResponse> topProducts = movementJpaRepository.findTopProducts(PageRequest.of(0, safeTopLimit))
            .stream()
            .map(row -> new DashboardTopProductResponse(
                String.valueOf(row[0]),
                String.valueOf(row[1]),
                String.valueOf(row[2]),
                safeLong((Number) row[3]),
                safeLong((Number) row[4])
            ))
            .toList();

        List<DashboardStockByLocationResponse> stockByLocation = inventoryJpaRepository.getStockByLocation().stream()
            .map(row -> new DashboardStockByLocationResponse(
                safeLong((Number) row[0]),
                String.valueOf(row[1]),
                String.valueOf(row[2]),
                safeLong((Number) row[3])
            ))
            .toList();

        int threshold = lowStockThreshold <= 0 ? DEFAULT_LOW_STOCK_THRESHOLD : lowStockThreshold;
        List<DashboardAlertResponse> alerts = inventoryJpaRepository.getLowStockProducts(threshold).stream()
            .map(row -> new DashboardAlertResponse(
                String.valueOf(row[0]),
                String.valueOf(row[1]),
                String.valueOf(row[2]),
                safeLong((Number) row[3]),
                threshold
            ))
            .toList();

        return ResponseEntity.ok(new DashboardResponse(overview, movementMetrics, topProducts, stockByLocation, alerts));
    }

    private long safeLong(Number value) {
        return value == null ? 0L : value.longValue();
    }
}
