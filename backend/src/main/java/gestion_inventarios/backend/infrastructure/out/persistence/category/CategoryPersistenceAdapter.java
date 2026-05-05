package gestion_inventarios.backend.infrastructure.out.persistence.category;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import gestion_inventarios.backend.application.ports.out.category.CategoryRepositoryPort;
import gestion_inventarios.backend.domain.model.category.Category;
import gestion_inventarios.backend.domain.model.shared.PageRequest;
import gestion_inventarios.backend.domain.model.shared.PageResult;
import gestion_inventarios.backend.infrastructure.out.persistence.category.entity.CategoryEntity;
import gestion_inventarios.backend.infrastructure.out.persistence.category.mapper.CategoryMapper;
import gestion_inventarios.backend.infrastructure.out.persistence.category.repository.CategoryJpaRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CategoryPersistenceAdapter implements CategoryRepositoryPort {

    private final CategoryJpaRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional(readOnly = true)
    public PageResult<Category> findAll(PageRequest pageRequest) {
        org.springframework.data.domain.PageRequest springPage =
            org.springframework.data.domain.PageRequest.of(pageRequest.page(), pageRequest.size(), Sort.by(Sort.Direction.ASC, "createdAt"));

        var page = categoryRepository.findAll(springPage);

        List<Category> content = page.getContent().stream()
            .map(categoryMapper::toDomain)
            .toList();

        return PageResult.of(content, pageRequest.page(), pageRequest.size(), page.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public Category findById(Long id) {
        return categoryRepository.findById(id)
            .map(categoryMapper::toDomain)
            .orElseThrow(() -> new gestion_inventarios.backend.domain.exception.CategoryNotFoundException(id));
    }

    @Override
    @Transactional
    public Category save(Category category) {
        CategoryEntity entity = categoryMapper.toEntity(category);
        return categoryMapper.toDomain(categoryRepository.save(entity));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasProducts(Long categoryId) {
        return categoryRepository.hasProducts(categoryId);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<Category> search(PageRequest pageRequest, String search) {
        org.springframework.data.domain.PageRequest springPage =
            org.springframework.data.domain.PageRequest.of(pageRequest.page(), pageRequest.size());

        var page = categoryRepository.search(springPage, search);

        List<Category> content = page.getContent().stream()
            .map(categoryMapper::toDomain)
            .toList();

        return PageResult.of(content, pageRequest.page(), pageRequest.size(), page.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Category> findByName(String name) {
        return categoryRepository.findByNameIgnoreCase(name)
            .map(categoryMapper::toDomain);
    }
}
