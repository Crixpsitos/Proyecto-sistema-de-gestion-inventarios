package gestion_inventarios.backend.application.ports.in;

import java.util.UUID;

import gestion_inventarios.backend.domain.model.products.Product;
import gestion_inventarios.backend.domain.model.shared.PageRequest;
import gestion_inventarios.backend.domain.model.shared.PageResult;

public interface ProductUseCase {
    PageResult<Product> findAll(PageRequest pageRequest);
    Product findById(UUID id);
    Product findBySku(String sku);
    Product save(Product product);
    void deleteById(UUID id);
    PageResult<Product> search(PageRequest pageRequest, String search);
    PageResult<Product> findByCategoryId(Long categoryId, PageRequest pageRequest);
    

}
