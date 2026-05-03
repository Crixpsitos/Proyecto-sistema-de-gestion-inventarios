package gestion_inventarios.backend.infrastructure.out.persistence.inventory.repository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import gestion_inventarios.backend.infrastructure.out.persistence.inventory.entity.InventoryEntity;

@Repository
public interface InventoryJpaRepository extends JpaRepository<InventoryEntity, Long> {

    @Query(value = """
            SELECT i.*
            FROM inventory i
            LEFT JOIN products p ON p.id = i.product_id
            LEFT JOIN locations l ON l.id = i.location_id
            WHERE LOWER(COALESCE(p.name, '')) LIKE LOWER(CONCAT('%', :term, '%'))
               OR LOWER(COALESCE(p.sku, '')) LIKE LOWER(CONCAT('%', :term, '%'))
               OR LOWER(COALESCE(l.name, '')) LIKE LOWER(CONCAT('%', :term, '%'))
               OR LOWER(COALESCE(l.code, '')) LIKE LOWER(CONCAT('%', :term, '%'))
               OR CAST(i.product_id AS VARCHAR) LIKE CONCAT('%', :term, '%')
               OR CAST(i.location_id AS VARCHAR) LIKE CONCAT('%', :term, '%')
            """, countQuery = """
            SELECT COUNT(*)
            FROM inventory i
            LEFT JOIN products p ON p.id = i.product_id
            LEFT JOIN locations l ON l.id = i.location_id
            WHERE LOWER(COALESCE(p.name, '')) LIKE LOWER(CONCAT('%', :term, '%'))
               OR LOWER(COALESCE(p.sku, '')) LIKE LOWER(CONCAT('%', :term, '%'))
               OR LOWER(COALESCE(l.name, '')) LIKE LOWER(CONCAT('%', :term, '%'))
               OR LOWER(COALESCE(l.code, '')) LIKE LOWER(CONCAT('%', :term, '%'))
               OR CAST(i.product_id AS VARCHAR) LIKE CONCAT('%', :term, '%')
               OR CAST(i.location_id AS VARCHAR) LIKE CONCAT('%', :term, '%')
            """, nativeQuery = true)
    Page<InventoryEntity> search(Pageable pageable, @Param("term") String term);

   @Query("SELECT COALESCE(SUM(i.quantity), 0) FROM InventoryEntity i WHERE i.locationId = :locationId")
   Long getCurrentStockByLocationId(@Param("locationId") Long locationId);

      @Query("SELECT COALESCE(SUM(i.quantity), 0) FROM InventoryEntity i")
      Long getTotalStock();

      @Query(value = """
            SELECT l.id, l.name, l.code, COALESCE(SUM(i.quantity), 0) AS total_stock
            FROM locations l
            LEFT JOIN inventory i ON i.location_id = l.id
            GROUP BY l.id, l.name, l.code
            ORDER BY total_stock DESC, l.name ASC
            """, nativeQuery = true)
      List<Object[]> getStockByLocation();

      @Query(value = """
            SELECT p.id, p.name, p.sku, COALESCE(SUM(i.quantity), 0) AS total_stock
            FROM products p
            LEFT JOIN inventory i ON i.product_id = p.id
            GROUP BY p.id, p.name, p.sku
            HAVING COALESCE(SUM(i.quantity), 0) <= :threshold
            ORDER BY total_stock ASC, p.name ASC
            """, nativeQuery = true)
      List<Object[]> getLowStockProducts(@Param("threshold") Integer threshold);

    Optional<InventoryEntity> findByProductIdAndLocationId(UUID productId, Long locationId);
}
