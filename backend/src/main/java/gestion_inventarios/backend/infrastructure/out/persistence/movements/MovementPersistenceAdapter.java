package gestion_inventarios.backend.infrastructure.out.persistence.movements;
import java.util.List;

import org.springframework.stereotype.Component;

import gestion_inventarios.backend.application.ports.out.movement.MovementRepositoryPort;
import gestion_inventarios.backend.domain.exception.MovementNotFoundException;
import gestion_inventarios.backend.domain.model.movements.Movement;
import gestion_inventarios.backend.domain.model.movements.MovementFilters;
import gestion_inventarios.backend.domain.model.shared.PageRequest;
import gestion_inventarios.backend.domain.model.shared.PageResult;
import gestion_inventarios.backend.infrastructure.out.persistence.movements.entity.MovementEntity;
import gestion_inventarios.backend.infrastructure.out.persistence.movements.mapper.MovementMapper;
import gestion_inventarios.backend.infrastructure.out.persistence.movements.repository.MovementJpaRepository;
import gestion_inventarios.backend.infrastructure.out.persistence.movements.specification.MovementSpecification;
import lombok.RequiredArgsConstructor;



@Component
@RequiredArgsConstructor
public class MovementPersistenceAdapter implements MovementRepositoryPort {

    private final MovementJpaRepository repository;
    private final MovementMapper mapper;
    


    @Override
    public Movement save(Movement movement) {
        MovementEntity entity = mapper.toEntity(movement);
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public Movement findById(Long id) {
        return repository.findById(id).map(mapper::toDomain).orElseThrow(() -> {
            throw new MovementNotFoundException(id);
        });
    }

    @Override
    public PageResult<Movement> findAll(PageRequest pageRequest, MovementFilters filters) {
        org.springframework.data.domain.PageRequest springPage = org.springframework.data.domain.PageRequest
                .of(pageRequest.page(), pageRequest.size());

     var page = repository.findAll(
                MovementSpecification.withFilters(filters),
                springPage
        );
        List<Movement> content = page.getContent().stream()
                .map(mapper::toDomain)
                .toList();

        return PageResult.of(content, pageRequest.page(), pageRequest.size(), page.getTotalElements());
    }

    

    
    
}
