package gestion_inventarios.backend.application.service;

import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gestion_inventarios.backend.application.ports.in.movement.MovementUseCase;
import gestion_inventarios.backend.domain.model.user.User;
import gestion_inventarios.backend.domain.model.inventory.Inventory;
import gestion_inventarios.backend.domain.model.locations.Location;
import gestion_inventarios.backend.domain.model.movements.Movement;
import gestion_inventarios.backend.domain.model.movements.MovementFilters;
import gestion_inventarios.backend.domain.model.movements.MovementType;
import gestion_inventarios.backend.domain.model.products.Product;
import gestion_inventarios.backend.domain.model.shared.PageRequest;
import gestion_inventarios.backend.domain.model.shared.PageResult;
import gestion_inventarios.backend.infrastructure.in.rest.movement.dto.MovementCreateRequest;
import gestion_inventarios.backend.infrastructure.out.persistence.inventory.InventoryPersistenceAdapter;
import gestion_inventarios.backend.infrastructure.out.persistence.movements.MovementPersistenceAdapter;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MovementService implements MovementUseCase {

    private final MovementPersistenceAdapter persistenceAdapter;
    private final InventoryPersistenceAdapter inventoryAdapter;
    private final ProductService productService;
    private final LocationService locationService;
    private final UserService userService;

    @Transactional
    @Override
    public Movement create(MovementCreateRequest request) {

        MovementType movementType = MovementType.valueOf(request.type());

        Product product = productService.findById(UUID.fromString(request.productId()));
        Location source = (request.sourceId() != null && !request.sourceId().isBlank())
                ? locationService.findById(Long.valueOf(request.sourceId()))
                : null;

        Location destination = (request.destinationId() != null && !request.destinationId().isBlank())
                ? locationService.findById(Long.valueOf(request.destinationId()))
                : null;

        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        User user = userService.findByEmail(userDetails.getUsername());

        String userId = user.getId().toString();
        Movement movement = new Movement(movementType, product, source, destination, request.quantity(),
                request.notes(), userId);

        validateMovement(movement);
        updateInventory(movement);
        return persistenceAdapter.save(movement);
    }

    @Transactional(readOnly = true)
    @Override
    public Movement findById(Long id) {
        return persistenceAdapter.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public PageResult<Movement> findAll(PageRequest pageRequest, MovementFilters filters) {
        return persistenceAdapter.findAll(pageRequest, filters);
    }

    private void updateInventory(Movement movement) {
        switch (movement.getType().name()) {
            case "IN" -> {

                addStock(movement.getProduct().getId(), movement.getDestination().getId(), movement.getQuantity());
            }
            case "OUT" -> {
                removeStock(movement.getProduct().getId(), movement.getSource().getId(), movement.getQuantity());
            }
            case "TRANSFER" -> {
                removeStock(movement.getProduct().getId(), movement.getSource().getId(), movement.getQuantity());
                addStock(movement.getProduct().getId(), movement.getDestination().getId(), movement.getQuantity());
            }
        }
    }

    private void addStock(UUID productId, Long locationId, Integer quantity) {
        Inventory inventory = inventoryAdapter
                .findByProductAndLocation(productId, locationId)
                .orElse(new Inventory(productId, locationId, 0));

        Inventory updated = new Inventory(
                inventory.getId(),
                inventory.getProductId(),
                inventory.getLocationId(),
                inventory.getQuantity() + quantity,
                inventory.getCreatedAt(),
                inventory.getUpdatedAt());

        inventoryAdapter.save(updated);
    }

    private void removeStock(UUID productId, Long locationId, Integer quantity) {
        Inventory inventory = inventoryAdapter
                .findByProductAndLocation(productId, locationId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No hay inventario de este producto en esta bodega"));

        if (inventory.getQuantity() < quantity) {
            throw new IllegalArgumentException(
                    "Stock insuficiente. Disponible: " + inventory.getQuantity());
        }

        Inventory updated = new Inventory(
                inventory.getId(),
                inventory.getProductId(),
                inventory.getLocationId(),
                inventory.getQuantity() - quantity,
                inventory.getCreatedAt(),
                inventory.getUpdatedAt());

        inventoryAdapter.save(updated);
    }

    private void validateMovement(Movement movement) {
        switch (movement.getType().name()) {
            case "ENTRY" -> {
                if (movement.getDestination() == null) {
                    throw new IllegalArgumentException(
                            "La bodega destino es obligatoria para ENTRY");
                }
            }
            case "EXIT" -> {
                if (movement.getSource() == null) {
                    throw new IllegalArgumentException(
                            "La bodega origen es obligatoria para EXIT");
                }
            }
            case "TRANSFER" -> {
                if (movement.getSource() == null || movement.getDestination() == null) {
                    throw new IllegalArgumentException(
                            "La bodega origen y destino son obligatorias para TRANSFER");
                }
                if (movement.getSource().equals(movement.getDestination())) {
                    throw new IllegalArgumentException(
                            "La bodega origen y destino no pueden ser iguales");
                }
            }
        }
    }
}
