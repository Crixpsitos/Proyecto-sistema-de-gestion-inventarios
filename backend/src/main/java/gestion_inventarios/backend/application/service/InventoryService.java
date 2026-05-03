package gestion_inventarios.backend.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gestion_inventarios.backend.application.ports.in.FindInventoryUseCase;
import gestion_inventarios.backend.application.ports.out.InventoryRepositoryPort;
import gestion_inventarios.backend.domain.model.inventory.Inventory;
import gestion_inventarios.backend.domain.model.shared.PageRequest;
import gestion_inventarios.backend.domain.model.shared.PageResult;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryService implements FindInventoryUseCase {

    private final InventoryRepositoryPort inventoryRepositoryPort;

    @Override
    @Transactional(readOnly = true)
    public PageResult<Inventory> findAll(PageRequest pageRequest) {
        return inventoryRepositoryPort.findAll(pageRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<Inventory> search(PageRequest pageRequest, String term) {
        return inventoryRepositoryPort.search(pageRequest, term);
    }
}
