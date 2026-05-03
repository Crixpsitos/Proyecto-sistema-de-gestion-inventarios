package gestion_inventarios.backend.infrastructure.out.persistence.location.mapper;

import org.springframework.stereotype.Component;

import gestion_inventarios.backend.domain.model.locations.Location;
import gestion_inventarios.backend.domain.model.locations.LocationCode;
import gestion_inventarios.backend.infrastructure.out.persistence.location.entity.LocationCodeEmbeddable;
import gestion_inventarios.backend.infrastructure.out.persistence.location.entity.LocationEntity;

@Component
public class LocationMapper {

    public Location toDomain(LocationEntity e) {
    if (e == null) return null; 
    return new Location(
        e.getId(),
        e.getName(),
        new LocationCode(e.getCode().getValue()),
        e.getType(),
        e.getAddress(),
        e.isActive(),
        e.getCreatedAt(),
        e.getUpdatedAt()
    );
}

public LocationEntity toEntity(Location l) {
    if (l == null) return null; 
    LocationCodeEmbeddable code = new LocationCodeEmbeddable(l.getCode().getValue());
    return new LocationEntity(
        l.getId(),
        l.getName(),
        code,
        l.getType(),
        l.getAddress(),
        l.isActive(),
        l.getCreatedAt(),
        l.getUpdatedAt()
    );
}

}
