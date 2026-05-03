package gestion_inventarios.backend.infrastructure.out.persistence.Category.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import gestion_inventarios.backend.infrastructure.out.persistence.Category.Entity.CategoryEntity;

public interface CategoryJpaRepository extends JpaRepository<CategoryEntity, Long> {

    @Query("SELECT c FROM CategoryEntity c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :term, '%')) OR LOWER(COALESCE(c.description, '')) LIKE LOWER(CONCAT('%', :term, '%'))")
    Page<CategoryEntity> search(Pageable pageable, @Param("term") String term);

    Optional<CategoryEntity> findByNameIgnoreCase(String name);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM ProductEntity p WHERE p.category.id = :categoryId")
    boolean hasProducts(@Param("categoryId") Long categoryId);

}
