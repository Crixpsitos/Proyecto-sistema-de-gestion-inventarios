package gestion_inventarios.backend.application.ports.out;

import java.util.Optional;
import java.util.UUID;

import gestion_inventarios.backend.domain.model.inventory.Inventory;
import gestion_inventarios.backend.domain.model.shared.PageRequest;
import gestion_inventarios.backend.domain.model.shared.PageResult;

public interface InventoryRepositoryPort {
    PageResult<Inventory> findAll(PageRequest pageRequest);
    PageResult<Inventory> search(PageRequest pageRequest, String term);
    Optional<Inventory> findByProductAndLocation(UUID productId, Long locationId);
    Inventory save(Inventory inventory);
}
