package gestion_inventarios.backend.application.ports.out;

import java.util.UUID;

import gestion_inventarios.backend.domain.model.products.Product;
import gestion_inventarios.backend.domain.model.shared.PageRequest;
import gestion_inventarios.backend.domain.model.shared.PageResult;

public interface ProductRepositoryPort {
    PageResult<Product> findAll(PageRequest pageRequest);
    Product findById(UUID id);
    Product save(Product product);
    void deleteById(UUID id);
    PageResult<Product> search(PageRequest pageRequest, String search);
    Product findBySku(String sku);
    PageResult<Product> findByCategoryId(Long categoryId, PageRequest pageRequest);
}
