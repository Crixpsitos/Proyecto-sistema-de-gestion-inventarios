package gestion_inventarios.backend.application.ports.in;

import gestion_inventarios.backend.domain.model.movements.Movement;
import gestion_inventarios.backend.domain.model.movements.MovementFilters;
import gestion_inventarios.backend.domain.model.shared.PageRequest;
import gestion_inventarios.backend.domain.model.shared.PageResult;
import gestion_inventarios.backend.infrastructure.in.rest.movement.dto.MovementCreateRequest;

public interface MovementUseCase {
    Movement create(MovementCreateRequest request);
    Movement findById(Long id);
    PageResult<Movement> findAll(PageRequest pageRequest, MovementFilters filters);
}