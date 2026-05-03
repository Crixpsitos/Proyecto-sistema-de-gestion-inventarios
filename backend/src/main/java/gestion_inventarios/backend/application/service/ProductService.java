package gestion_inventarios.backend.application.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import gestion_inventarios.backend.application.ports.in.ProductUseCase;
import gestion_inventarios.backend.application.ports.out.ProductRepositoryPort;
import gestion_inventarios.backend.domain.model.products.Product;
import gestion_inventarios.backend.domain.model.shared.PageRequest;
import gestion_inventarios.backend.domain.model.shared.PageResult;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService implements ProductUseCase {

    private final ProductRepositoryPort productRepository;

    @Override
    public PageResult<Product> findAll(PageRequest pageRequest) {
        return productRepository.findAll(pageRequest);
    }

    @Override
    public Product findById(UUID id) {
        return productRepository.findById(id);
    }

    @Override
    public Product findBySku(String sku) {
        return productRepository.findBySku(sku);
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void deleteById(UUID id) {
        productRepository.deleteById(id);
    }

    @Override
    public PageResult<Product> search(PageRequest pageRequest, String search) {
        return productRepository.search(pageRequest, search);
       
    }

    @Override
    public PageResult<Product> findByCategoryId(Long categoryId, PageRequest pageRequest) {
        return productRepository.findByCategoryId(categoryId, pageRequest);
    }


}
