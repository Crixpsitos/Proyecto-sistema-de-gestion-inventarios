package gestion_inventarios.backend.infrastructure.in.rest.employee.dto;

import java.util.List;

public record EmployeeCatalogResponse(
    List<EmployeeOptionResponse> roles,
    List<EmployeeOptionResponse> permissions
) {}
