package gestion_inventarios.backend.application.ports.out;


import gestion_inventarios.backend.domain.model.movements.Movement;
import gestion_inventarios.backend.domain.model.movements.MovementFilters;
import gestion_inventarios.backend.domain.model.shared.PageRequest;
import gestion_inventarios.backend.domain.model.shared.PageResult;

public interface MovementRepositoryPort {
    Movement save(Movement movement);
    Movement findById(Long id);
    PageResult<Movement> findAll(PageRequest pageRequest, MovementFilters filters);
}
