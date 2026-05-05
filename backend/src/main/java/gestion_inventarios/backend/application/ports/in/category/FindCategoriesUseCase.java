package gestion_inventarios.backend.application.ports.in.category;

import gestion_inventarios.backend.domain.model.category.Category;
import gestion_inventarios.backend.domain.model.shared.PageRequest;
import gestion_inventarios.backend.domain.model.shared.PageResult;

public interface FindCategoriesUseCase {
    PageResult<Category> findAll(PageRequest pageRequest);
    Category findById(Long id);
}
