package gestion_inventarios.backend.infrastructure.in.rest.inventory.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

import gestion_inventarios.backend.application.ports.in.FindInventoryUseCase;
import gestion_inventarios.backend.application.ports.in.LocationUseCase;
import gestion_inventarios.backend.application.ports.in.ProductUseCase;
import gestion_inventarios.backend.domain.model.inventory.Inventory;
import gestion_inventarios.backend.domain.model.locations.Location;
import gestion_inventarios.backend.domain.model.products.Product;
import gestion_inventarios.backend.domain.model.shared.PageRequest;
import gestion_inventarios.backend.domain.model.shared.PageResult;
import gestion_inventarios.backend.infrastructure.in.rest.inventory.dto.InventoryResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final FindInventoryUseCase findInventoryUseCase;
    private final ProductUseCase productUseCase;
    private final LocationUseCase locationUseCase;

    @GetMapping
    public ResponseEntity<PageResult<InventoryResponse>> getAll(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        PageResult<Inventory> result =
            findInventoryUseCase.findAll(new PageRequest(page, size));

        PageResult<InventoryResponse> response = PageResult.of(
            result.content().stream().map(item -> {
                Product product = findProductSafely(item.getProductId());
                Location location = findLocationSafely(item.getLocationId());

                String productSku = product != null ? product.getSku() : "N/A";
                String productName = product != null ? product.getName() : "Producto no encontrado";
                String locationCode = location != null ? location.getCode().getValue() : "N/A";
                String locationName = location != null ? location.getName() : "Ubicación no encontrada";

                return InventoryResponse.from(item, productSku, productName, locationCode, locationName);
            }).toList(),
            result.page(),
            result.size(),
            result.totalElements()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<PageResult<InventoryResponse>> search(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(value = "q", required = true) String search
    ) {
        PageResult<Inventory> result =
            findInventoryUseCase.search(new PageRequest(page, size), search);

        PageResult<InventoryResponse> response = PageResult.of(
            result.content().stream().map(item -> {
                Product product = findProductSafely(item.getProductId());
                Location location = findLocationSafely(item.getLocationId());

                String productSku = product != null ? product.getSku() : "N/A";
                String productName = product != null ? product.getName() : "Producto no encontrado";
                String locationCode = location != null ? location.getCode().getValue() : "N/A";
                String locationName = location != null ? location.getName() : "Ubicación no encontrada";

                return InventoryResponse.from(item, productSku, productName, locationCode, locationName);
            }).toList(),
            result.page(),
            result.size(),
            result.totalElements()
        );

        return ResponseEntity.ok(response);
    }

    private Product findProductSafely(UUID productId) {
        try {
            return productUseCase.findById(productId);
        } catch (RuntimeException ex) {
            return null;
        }
    }

    private Location findLocationSafely(Long locationId) {
        try {
            return locationUseCase.findById(locationId);
        } catch (RuntimeException ex) {
            return null;
        }
    }
}
