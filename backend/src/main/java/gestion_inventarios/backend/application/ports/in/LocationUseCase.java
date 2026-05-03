package gestion_inventarios.backend.application.ports.in;

import gestion_inventarios.backend.domain.model.locations.Location;
import gestion_inventarios.backend.domain.model.locations.LocationType;
import gestion_inventarios.backend.domain.model.shared.PageRequest;
import gestion_inventarios.backend.domain.model.shared.PageResult;

public interface LocationUseCase {

    Location create(String name, String code, LocationType type, String address, boolean active);
    Location update(Long id, String name, LocationType type, String address);
    void deactivate(Long id);
    void activate(Long id);
    Location findById(Long id);
    boolean isCodeAvailable(String code);
    PageResult<Location> findByType(PageRequest pageRequest, LocationType type);
    PageResult<Location> findAll(PageRequest pageRequest, String type, Boolean isActive);
    PageResult<Location> findAllActive(PageRequest pageRequest);

    PageResult<Location> search(PageRequest pageRequest, String search);

}