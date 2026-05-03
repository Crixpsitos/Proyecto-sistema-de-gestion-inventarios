package gestion_inventarios.backend.application.ports.out;

import java.util.Optional;

import gestion_inventarios.backend.domain.model.category.Category;
import gestion_inventarios.backend.domain.model.shared.PageRequest;
import gestion_inventarios.backend.domain.model.shared.PageResult;

public interface CategoryRepositoryPort {
    PageResult<Category> findAll(PageRequest pageRequest);
    Category findById(Long id);
    Category save(Category category);
    void deleteById(Long id);
    boolean hasProducts(Long categoryId);
    PageResult<Category> search(PageRequest pageRequest, String search);
    Optional<Category> findByName(String name);
}
