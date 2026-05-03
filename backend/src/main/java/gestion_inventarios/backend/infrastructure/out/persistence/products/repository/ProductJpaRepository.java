package gestion_inventarios.backend.infrastructure.out.persistence.products.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import gestion_inventarios.backend.infrastructure.out.persistence.products.entity.ProductEntity;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, UUID> {

 Optional<ProductEntity> findBySku(String sku);

 Page<ProductEntity> findByCategoryId(Long categoryId, Pageable pageable);

    @Query("SELECT p FROM ProductEntity p WHERE " +
           "LOWER(p.sku) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.brand) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.model) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.color) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.size) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "ORDER BY p.createdAt DESC")
    Page<ProductEntity> search(Pageable pageable, @Param("searchTerm") String searchTerm);
}
