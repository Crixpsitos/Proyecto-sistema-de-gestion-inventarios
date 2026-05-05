package gestion_inventarios.backend.infrastructure.in.rest.movement.rest;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gestion_inventarios.backend.application.ports.in.user.FindUserUseCase;
import gestion_inventarios.backend.application.ports.in.movement.MovementUseCase;
import gestion_inventarios.backend.domain.model.user.User;
import gestion_inventarios.backend.domain.model.movements.Movement;
import gestion_inventarios.backend.domain.model.movements.MovementFilters;
import gestion_inventarios.backend.domain.model.movements.MovementType;
import gestion_inventarios.backend.domain.model.shared.PageRequest;
import gestion_inventarios.backend.domain.model.shared.PageResult;
import gestion_inventarios.backend.infrastructure.in.rest.movement.dto.MovementCreateRequest;
import gestion_inventarios.backend.infrastructure.in.rest.movement.dto.MovementResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/movements")
public class MovementController {

    private final MovementUseCase movementUseCase;
    private final FindUserUseCase findUserCase;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','EMPLOYEE')")
    public ResponseEntity<PageResult<MovementResponse>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) MovementType type,
            @RequestParam(required = false) UUID productId,
            @RequestParam(required = false) Long locationId,
            @RequestParam(required = false) LocalDate dateFrom,
            @RequestParam(required = false) LocalDate dateTo) {

        PageRequest pageRequest = new PageRequest(page, size);
        MovementFilters filters = new MovementFilters(type, productId, locationId, dateFrom, dateTo);

        PageResult<Movement> movements = movementUseCase.findAll(pageRequest, filters);

        PageResult<MovementResponse> response = PageResult.of(
            movements.content().stream().map(this::toMovementResponse).toList(),
                movements.page(), movements.size(), movements.totalElements());

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','EMPLOYEE')")
    public ResponseEntity<MovementResponse> create(@RequestBody @Valid MovementCreateRequest movementRequest) {
        

        Movement createdMovement = movementUseCase.create(movementRequest);

        return ResponseEntity.ok(toMovementResponse(createdMovement));

    }

    private MovementResponse toMovementResponse(Movement movement) {
        String createdByDisplay = movement.getCreatedBy();
        try {
            Long createdById = Long.valueOf(movement.getCreatedBy());
            User user = findUserCase.findById(createdById);
            createdByDisplay = String.format("%s %s", user.getName(), user.getLastName()).trim();
        } catch (RuntimeException ignored) {
            // Si no es numérico o no existe usuario, se conserva el valor original
        }

        return new MovementResponse(
                movement.getId(),
                gestion_inventarios.backend.infrastructure.in.rest.movement.dto.MovementTypeDTO.from(
                        movement.getType().getDisplayName(),
                        movement.getType().name()),
                movement.getProduct(),
                movement.getSource(),
                movement.getDestination(),
                movement.getQuantity(),
                movement.getNotes(),
                createdByDisplay,
                movement.getCreatedAt().toString());
    }

}
