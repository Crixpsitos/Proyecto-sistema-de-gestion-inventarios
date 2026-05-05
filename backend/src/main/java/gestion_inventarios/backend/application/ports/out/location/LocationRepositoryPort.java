package gestion_inventarios.backend.application.ports.out.location;

import gestion_inventarios.backend.domain.model.locations.Location;
import gestion_inventarios.backend.domain.model.locations.LocationCode;
import gestion_inventarios.backend.domain.model.locations.LocationType;
import gestion_inventarios.backend.domain.model.shared.PageRequest;
import gestion_inventarios.backend.domain.model.shared.PageResult;

public interface LocationRepositoryPort {
    Location findById(Long id);
    boolean existsByCode(LocationCode code);
    PageResult<Location> findByType(PageRequest pageRequest, LocationType type);
    PageResult<Location> search(PageRequest pageRequest, String search);
    PageResult<Location> findAllActive(PageRequest pageRequest);
    PageResult<Location> findAll(PageRequest pageRequest, String type, Boolean isActive);
    void deactivate(Long id);
    void activate(Long id);
    Location save(Location location);
}
