package gestion_inventarios.backend.infrastructure.out.persistence.movements.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import gestion_inventarios.backend.domain.model.movements.MovementType;
import gestion_inventarios.backend.infrastructure.out.persistence.movements.entity.MovementEntity;

public interface MovementJpaRepository extends JpaRepository<MovementEntity, Long>,  JpaSpecificationExecutor<MovementEntity> {
    
@Query("""
    SELECT m FROM MovementEntity m
    WHERE (:type IS NULL OR m.type = :type)
    AND (:productId IS NULL OR m.product.id = :productId)
    AND (:locationId IS NULL OR m.source.id = :locationId OR m.destination.id = :locationId)
    AND (:dateFrom IS NULL OR CAST(m.createdAt AS date) >= :dateFrom)
    AND (:dateTo IS NULL OR CAST(m.createdAt AS date) <= :dateTo)
    ORDER BY m.createdAt DESC
""")
Page<MovementEntity> findAll(
    @Param("type") MovementType type,
    @Param("productId") UUID productId,
    @Param("locationId") Long locationId,
    @Param("dateFrom") LocalDate dateFrom,
    @Param("dateTo") LocalDate dateTo,
    Pageable pageable
);

@Query("""
    SELECT COUNT(m)
    FROM MovementEntity m
    WHERE (m.source IS NOT NULL AND m.source.id = :locationId)
       OR (m.destination IS NOT NULL AND m.destination.id = :locationId)
""")
Long countByLocationId(@Param("locationId") Long locationId);

@Query("SELECT COUNT(m) FROM MovementEntity m WHERE m.createdAt BETWEEN :start AND :end")
Long countByCreatedAtBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

@Query("SELECT COUNT(m) FROM MovementEntity m WHERE m.type = :type AND m.createdAt BETWEEN :start AND :end")
Long countByTypeAndCreatedAtBetween(@Param("type") MovementType type, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

@Query("SELECT MAX(m.createdAt) FROM MovementEntity m")
LocalDateTime findLastMovementAt();

@Query("""
    SELECT m.product.id, m.product.name, m.product.sku, SUM(m.quantity), COUNT(m)
    FROM MovementEntity m
    GROUP BY m.product.id, m.product.name, m.product.sku
    ORDER BY SUM(m.quantity) DESC
""")
List<Object[]> findTopProducts(Pageable pageable);
}
