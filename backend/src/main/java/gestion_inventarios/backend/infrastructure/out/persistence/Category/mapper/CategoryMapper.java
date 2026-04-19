package gestion_inventarios.backend.infrastructure.out.persistence.Category.mapper;

import org.springframework.stereotype.Component;

import gestion_inventarios.backend.domain.model.category.Category;
import gestion_inventarios.backend.infrastructure.out.persistence.Category.Entity.CategoryEntity;

@Component
public class CategoryMapper {

    public Category toDomain(CategoryEntity e) {
        return new Category(e.getId(), e.getName(), e.getDescription(), e.getCreatedAt(), e.getUpdatedAt());
    }

    public CategoryEntity toEntity(Category c) {
        return new CategoryEntity(c.getId(), c.getName(), c.getDescription(), c.getCreatedAt(), c.getUpdatedAt());
    }
}
