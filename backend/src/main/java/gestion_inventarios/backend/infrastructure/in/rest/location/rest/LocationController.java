package gestion_inventarios.backend.infrastructure.in.rest.location.rest;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gestion_inventarios.backend.application.ports.in.LocationUseCase;
import gestion_inventarios.backend.domain.model.locations.Location;
import gestion_inventarios.backend.domain.model.locations.LocationType;
import gestion_inventarios.backend.domain.model.shared.PageRequest;
import gestion_inventarios.backend.domain.model.shared.PageResult;
import gestion_inventarios.backend.infrastructure.in.rest.location.dto.LocationCreateRequest;
import gestion_inventarios.backend.infrastructure.in.rest.location.dto.LocationResponse;
import gestion_inventarios.backend.infrastructure.in.rest.location.dto.LocationResponseWithDisplayName;
import gestion_inventarios.backend.infrastructure.in.rest.location.dto.LocationTypeOption;
import gestion_inventarios.backend.infrastructure.out.persistence.inventory.repository.InventoryJpaRepository;
import gestion_inventarios.backend.infrastructure.out.persistence.movements.repository.MovementJpaRepository;
import gestion_inventarios.backend.shared.dto.EntityExistResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationController {

        private final LocationUseCase locationUseCase;
        private final InventoryJpaRepository inventoryJpaRepository;
        private final MovementJpaRepository movementJpaRepository;

        @GetMapping
        @PreAuthorize("hasAnyRole('ADMIN','MANAGER','EMPLOYEE')")
        public ResponseEntity<PageResult<LocationResponseWithDisplayName>> getAll(
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "all") String type,
                        @RequestParam(defaultValue = "true") Boolean onlyActive

        ) {
                PageResult<Location> result = locationUseCase.findAll(new PageRequest(page, size), type, onlyActive);

                PageResult<LocationResponseWithDisplayName> response = PageResult.of(
                                result.content().stream().map(location -> {
                                        Long movementCount = movementJpaRepository.countByLocationId(location.getId());
                                        Long currentStock = inventoryJpaRepository.getCurrentStockByLocationId(location.getId());
                                        return LocationResponseWithDisplayName.from(location, movementCount, currentStock);
                                }).toList(),
                                result.page(), result.size(), result.totalElements());

                return ResponseEntity.ok(response);

        }

        @GetMapping("/{id}")
        @PreAuthorize("hasAnyRole('ADMIN','MANAGER','EMPLOYEE')")
        public ResponseEntity<LocationResponse> getById(@PathVariable Long id) {
                Location location = locationUseCase.findById(id);
                Long movementCount = movementJpaRepository.countByLocationId(location.getId());
                Long currentStock = inventoryJpaRepository.getCurrentStockByLocationId(location.getId());
                return ResponseEntity.ok(LocationResponse.from(location, movementCount, currentStock));
        }

        @GetMapping("/search")
        @PreAuthorize("hasAnyRole('ADMIN','MANAGER','EMPLOYEE')")
        public ResponseEntity<PageResult<LocationResponse>> search(
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(value = "q", required = true, defaultValue = "") String search) {
                PageResult<Location> result = locationUseCase.search(new PageRequest(page, size), search);

                PageResult<LocationResponse> response = PageResult.of(
                                result.content().stream().map(location -> {
                                        Long movementCount = movementJpaRepository.countByLocationId(location.getId());
                                        Long currentStock = inventoryJpaRepository.getCurrentStockByLocationId(location.getId());
                                        return LocationResponse.from(location, movementCount, currentStock);
                                }).toList(),
                                result.page(), result.size(), result.totalElements());

                return ResponseEntity.ok(response);
        }

        @GetMapping("/types")
        @PreAuthorize("hasAnyRole('ADMIN','MANAGER','EMPLOYEE')")
        public ResponseEntity<List<LocationTypeOption>> getLocationTypes() {

                List<LocationTypeOption> types = Arrays.stream(LocationType.values())
                                .map(LocationTypeOption::from)
                                .toList();
                return ResponseEntity.ok(types);
        }

        @GetMapping("/check-code")
        @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
        public ResponseEntity<EntityExistResponse> isCodeAvailable(
                        @RequestParam(value = "code", required = true) String code) {
                return ResponseEntity.ok(EntityExistResponse.from(locationUseCase.isCodeAvailable(code)));
        }

        @PostMapping
        @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
        public ResponseEntity<LocationResponse> createLocation(@RequestBody @Valid LocationCreateRequest location) {

                Location createdLocation = locationUseCase.create(location.name(), location.code(), location.type(),
                                location.address(), location.active());
                return ResponseEntity.ok(LocationResponse.from(createdLocation, 0L, 0L));
        }

        @PatchMapping("/{id}/activate")
        @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
        public ResponseEntity<Void> activateLocation(@PathVariable Long id) {
                locationUseCase.activate(id);
                return ResponseEntity.noContent().build();
        }

        @PatchMapping("/{id}/deactivate")
        @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
        public ResponseEntity<Void> deactivateLocation(@PathVariable Long id) {
                locationUseCase.deactivate(id);
                return ResponseEntity.noContent().build();
        }

        @PatchMapping("/{id}")
        @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
        public ResponseEntity<LocationResponse> updateLocation(@PathVariable Long id,
                        @RequestBody @Valid LocationCreateRequest request) {
                Location updatedLocation = locationUseCase.update(id, request.name(), request.type(),
                                request.address());
                Long movementCount = movementJpaRepository.countByLocationId(updatedLocation.getId());
                Long currentStock = inventoryJpaRepository.getCurrentStockByLocationId(updatedLocation.getId());
                return ResponseEntity.ok(LocationResponse.from(updatedLocation, movementCount, currentStock));
                        }

}
