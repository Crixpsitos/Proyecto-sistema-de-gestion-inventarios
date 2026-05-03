package gestion_inventarios.backend.infrastructure.out.persistence.inventory;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

import org.springframework.stereotype.Component;

import gestion_inventarios.backend.application.ports.out.InventoryRepositoryPort;
import gestion_inventarios.backend.domain.model.inventory.Inventory;
import gestion_inventarios.backend.domain.model.shared.PageRequest;
import gestion_inventarios.backend.domain.model.shared.PageResult;
import gestion_inventarios.backend.infrastructure.out.persistence.inventory.entity.InventoryEntity;
import gestion_inventarios.backend.infrastructure.out.persistence.inventory.mapper.InventoryMapper;
import gestion_inventarios.backend.infrastructure.out.persistence.inventory.repository.InventoryJpaRepository;
import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class InventoryPersistenceAdapter implements InventoryRepositoryPort {

    private final InventoryJpaRepository repository;
    private final InventoryMapper mapper;

    @Override
    public PageResult<Inventory> findAll(PageRequest pageRequest) {
        org.springframework.data.domain.PageRequest springPage =
            org.springframework.data.domain.PageRequest.of(pageRequest.page(), pageRequest.size());

        var page = repository.findAll(springPage);

        List<Inventory> content = page.getContent().stream()
            .map(mapper::toDomain)
            .toList();

        return PageResult.of(content, pageRequest.page(), pageRequest.size(), page.getTotalElements());
    }

    @Override
    public PageResult<Inventory> search(PageRequest pageRequest, String term) {
        org.springframework.data.domain.PageRequest springPage =
            org.springframework.data.domain.PageRequest.of(pageRequest.page(), pageRequest.size());

        var page = repository.search(springPage, term);

        List<Inventory> content = page.getContent().stream()
            .map(mapper::toDomain)
            .toList();

        return PageResult.of(content, pageRequest.page(), pageRequest.size(), page.getTotalElements());
    }

    @Override
    public Optional<Inventory> findByProductAndLocation(UUID productId, Long locationId) {
        return repository.findByProductIdAndLocationId(productId, locationId)
                .map(mapper::toDomain);
    }

    @Override
    public Inventory save(Inventory inventory) {
        InventoryEntity entity = mapper.toEntity(inventory);
        return mapper.toDomain(repository.save(entity));
    }
}