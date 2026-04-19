package gestion_inventarios.backend.infrastructure.in.rest.category.dto;

import java.time.LocalDateTime;

import gestion_inventarios.backend.domain.model.category.Category;

public record CategoryResponse(
    Long id,
    String name,
    String description,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static CategoryResponse from(Category category) {
        return new CategoryResponse(
            category.getId(),
            category.getName(),
            category.getDescription(),
            category.getCreatedAt(),
            category.getUpdatedAt()
        );
    }
}
