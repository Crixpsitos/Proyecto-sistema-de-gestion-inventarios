package gestion_inventarios.backend.infrastructure.in.rest.location.dto;

import gestion_inventarios.backend.domain.model.locations.LocationType;

public record LocationTypeOption(
    String value,
    String label
) {
    public static LocationTypeOption from(LocationType type) {
        return new LocationTypeOption(type.name(), type.getDisplayName());
    }
}
