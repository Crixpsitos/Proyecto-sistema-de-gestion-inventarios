package gestion_inventarios.backend.application.service;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import gestion_inventarios.backend.application.ports.in.DeleteCategoryUseCase;
import gestion_inventarios.backend.application.ports.in.FindCategoriesUseCase;
import gestion_inventarios.backend.application.ports.in.FindSearchCategoriesUseCase;
import gestion_inventarios.backend.application.ports.in.SaveCategoryUseCase;
import gestion_inventarios.backend.application.ports.out.CategoryRepositoryPort;
import gestion_inventarios.backend.domain.exception.CategoryNotFoundException;
import gestion_inventarios.backend.domain.exception.DuplicateCategoryException;
import gestion_inventarios.backend.domain.model.category.Category;
import gestion_inventarios.backend.domain.model.shared.PageRequest;
import gestion_inventarios.backend.domain.model.shared.PageResult;
import gestion_inventarios.backend.infrastructure.in.rest.category.dto.CategoryRequest;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class CategoryService implements FindCategoriesUseCase, FindSearchCategoriesUseCase, SaveCategoryUseCase, DeleteCategoryUseCase {
    private final CategoryRepositoryPort categoryRepository;

    @Override
    public PageResult<Category> findAll(PageRequest pageRequest) {
        return categoryRepository.findAll(pageRequest);
    }

    @Override
    public PageResult<Category> search(PageRequest pageRequest, String search) {
        return categoryRepository.search(pageRequest, search);
    }

    @Override
    public Category save(CategoryRequest request) {
        if (request.getId() == null) {
            // Crear
            categoryRepository.findByName(request.getName()).ifPresent(category -> {
                throw new DuplicateCategoryException("La categoría con el nombre '" + request.getName() + "' ya existe");
            });
            LocalDateTime now = LocalDateTime.now();
            Category category = new Category(null, request.getName(), request.getDescription(), now, now);
            return categoryRepository.save(category);
        } else {
            // Actualizar
            Category existing = categoryRepository.findById(request.getId());
            if (!existing.getName().equalsIgnoreCase(request.getName())) {
                categoryRepository.findByName(request.getName()).ifPresent(category -> {
                    throw new DuplicateCategoryException("La categoría con el nombre '" + request.getName() + "' ya existe");
                });
            }
            LocalDateTime now = LocalDateTime.now();
            Category category = new Category(request.getId(), request.getName(), request.getDescription(), existing.getCreatedAt(), now);
            return categoryRepository.save(category);
        }
    }

    @Override
    public Category findById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        Category categoryExist = categoryRepository.findById(id);

        if (categoryExist == null) {
            throw new CategoryNotFoundException(id);
        }
            
        categoryRepository.deleteById(id);
    }
    
}
