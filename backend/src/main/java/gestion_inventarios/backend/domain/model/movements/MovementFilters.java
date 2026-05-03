package gestion_inventarios.backend.domain.model.movements;

import java.time.LocalDate;
import java.util.UUID;

public record MovementFilters(
    MovementType type,
    UUID productId,
    Long locationId,
    LocalDate dateFrom,
    LocalDate dateTo
) {
    
}
