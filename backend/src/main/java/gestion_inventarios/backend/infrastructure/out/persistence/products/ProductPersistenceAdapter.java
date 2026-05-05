package gestion_inventarios.backend.infrastructure.out.persistence.products;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import gestion_inventarios.backend.application.ports.out.product.ProductRepositoryPort;
import gestion_inventarios.backend.domain.model.products.Product;
import gestion_inventarios.backend.domain.model.shared.PageRequest;
import gestion_inventarios.backend.domain.model.shared.PageResult;
import gestion_inventarios.backend.infrastructure.out.persistence.products.entity.ProductEntity;
import gestion_inventarios.backend.infrastructure.out.persistence.products.mappers.ProductMapper;
import gestion_inventarios.backend.infrastructure.out.persistence.products.repository.ProductJpaRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductPersistenceAdapter implements ProductRepositoryPort {

    private final ProductJpaRepository productJpaRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional(readOnly = true)
    public PageResult<Product> findAll(PageRequest pageRequest) {
        org.springframework.data.domain.PageRequest springPage = org.springframework.data.domain.PageRequest
                .of(pageRequest.page(), pageRequest.size(), Sort.by(Sort.Direction.ASC, "createdAt"));

        var page = productJpaRepository.findAll(springPage);

        List<Product> content = page.getContent().stream()
                .map(productMapper::toDomain)
                .toList();

        return PageResult.of(content, pageRequest.page(), pageRequest.size(), page.getTotalElements());
    }

    @Override
    public Product findById(UUID id) {
        return productJpaRepository.findById(id)
                .map(productMapper::toDomain)
                .orElseThrow(() -> new gestion_inventarios.backend.domain.exception.ProductNotFoundException(id));
    }

    @Override
    public Product save(Product product) {
        ProductEntity entity = productMapper.toEntity(product);
        return productMapper.toDomain(productJpaRepository.save(entity));
    }

    @Override
    public void deleteById(UUID id) {
        productJpaRepository.deleteById(id);
    }

    @Override
    public PageResult<Product> search(PageRequest pageRequest, String search) {
        org.springframework.data.domain.PageRequest springPage = org.springframework.data.domain.PageRequest
                .of(pageRequest.page(), pageRequest.size(), Sort.by(Sort.Direction.ASC, "createdAt"));

        var page = productJpaRepository.search(springPage, search);

        List<Product> content = page.getContent().stream()
                .map(productMapper::toDomain)
                .toList();

        return PageResult.of(content, pageRequest.page(), pageRequest.size(), page.getTotalElements());
    }

    @Override
    public Product findBySku(String sku) {
        return productJpaRepository.findBySku(sku)
                .map(productMapper::toDomain)
                .orElseThrow(
                        () -> new gestion_inventarios.backend.domain.exception.ProductNotFoundException("SKU: " + sku));
    }

    @Override
    public PageResult<Product> findByCategoryId(Long categoryId, PageRequest pageRequest) {
        org.springframework.data.domain.PageRequest springPage = org.springframework.data.domain.PageRequest
                .of(pageRequest.page(), pageRequest.size(), Sort.by(Sort.Direction.ASC, "createdAt"));

        var page = productJpaRepository.findByCategoryId(categoryId, springPage);

        List<Product> content = page.getContent().stream()
                .map(productMapper::toDomain)
                .toList();

        return PageResult.of(content, pageRequest.page(), pageRequest.size(), page.getTotalElements());
    }

}
