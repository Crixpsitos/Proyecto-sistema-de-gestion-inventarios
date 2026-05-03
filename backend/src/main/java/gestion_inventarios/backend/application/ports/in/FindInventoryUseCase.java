package gestion_inventarios.backend.application.ports.in;

import gestion_inventarios.backend.domain.model.inventory.Inventory;
import gestion_inventarios.backend.domain.model.shared.PageRequest;
import gestion_inventarios.backend.domain.model.shared.PageResult;

public interface FindInventoryUseCase {
    PageResult<Inventory> findAll(PageRequest pageRequest);
    PageResult<Inventory> search(PageRequest pageRequest, String term);
}
