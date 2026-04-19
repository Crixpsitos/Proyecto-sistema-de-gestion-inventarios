package gestion_inventarios.backend.application.ports.in;

import gestion_inventarios.backend.domain.model.category.Category;
import gestion_inventarios.backend.domain.model.shared.PageRequest;
import gestion_inventarios.backend.domain.model.shared.PageResult;

public interface FindSearchCategoriesUseCase {
    PageResult<Category> search(PageRequest pageRequest, String search);

    
}
