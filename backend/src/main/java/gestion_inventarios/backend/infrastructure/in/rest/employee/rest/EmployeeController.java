package gestion_inventarios.backend.infrastructure.in.rest.employee.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gestion_inventarios.backend.application.ports.in.EmployeeUseCase;
import gestion_inventarios.backend.domain.model.User;
import gestion_inventarios.backend.domain.model.shared.PageRequest;
import gestion_inventarios.backend.domain.model.shared.PageResult;
import gestion_inventarios.backend.infrastructure.in.rest.employee.dto.EmployeeCreateRequest;
import gestion_inventarios.backend.infrastructure.in.rest.employee.dto.EmployeePermissionsRequest;
import gestion_inventarios.backend.infrastructure.in.rest.employee.dto.EmployeeResponse;
import gestion_inventarios.backend.infrastructure.in.rest.employee.dto.EmployeeUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeUseCase employeeUseCase;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<PageResult<EmployeeResponse>> findAll(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "") String search
    ) {
        PageResult<User> result = employeeUseCase.search(new PageRequest(page, size), search);

        PageResult<EmployeeResponse> response = PageResult.of(
            result.content().stream().map(EmployeeResponse::from).toList(),
            result.page(),
            result.size(),
            result.totalElements()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<EmployeeResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(EmployeeResponse.from(employeeUseCase.findById(id)));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<EmployeeResponse> create(@Valid @RequestBody EmployeeCreateRequest request) {
        User created = employeeUseCase.create(
            request.name(),
            request.lastName(),
            request.email(),
            request.password(),
            request.phone(),
            request.documentType(),
            request.documentNumber(),
            request.role()
        );

        return ResponseEntity.ok(EmployeeResponse.from(created));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<EmployeeResponse> updateProfile(@PathVariable Long id, @Valid @RequestBody EmployeeUpdateRequest request) {
        User updated = employeeUseCase.updateProfile(
            id,
            request.name(),
            request.lastName(),
            request.email(),
            request.phone(),
            request.documentType(),
            request.documentNumber()
        );

        return ResponseEntity.ok(EmployeeResponse.from(updated));
    }

    @PatchMapping("/{id}/permissions")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<EmployeeResponse> updateRoleAndPermissions(@PathVariable Long id, @Valid @RequestBody EmployeePermissionsRequest request) {
        User updated = employeeUseCase.updateRoleAndPermissions(
            id,
            request.role()
        );

        return ResponseEntity.ok(EmployeeResponse.from(updated));
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<EmployeeResponse> activate(@PathVariable Long id) {
        return ResponseEntity.ok(EmployeeResponse.from(employeeUseCase.activate(id)));
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<EmployeeResponse> deactivate(@PathVariable Long id) {
        return ResponseEntity.ok(EmployeeResponse.from(employeeUseCase.deactivate(id)));
    }
}
