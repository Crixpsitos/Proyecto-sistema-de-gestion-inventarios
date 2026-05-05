package gestion_inventarios.backend.application.ports.in.category;

import gestion_inventarios.backend.domain.model.category.Category;
import gestion_inventarios.backend.infrastructure.in.rest.category.dto.UpdateCategoryRequest;

public interface UpdateCategoryUseCase {
    Category update(Long id, UpdateCategoryRequest request);
}
