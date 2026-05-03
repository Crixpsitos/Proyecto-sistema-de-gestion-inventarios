package gestion_inventarios.backend.infrastructure.out.persistence.location.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import gestion_inventarios.backend.domain.model.locations.LocationType;
import gestion_inventarios.backend.infrastructure.out.persistence.location.entity.LocationEntity;


public interface LocationJpaRepository extends JpaRepository<LocationEntity, Long> {

    @Transactional(readOnly = true)
    @Query("SELECT l FROM LocationEntity l WHERE " +
            "LOWER(l.name) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(l.address) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(l.code.value) LIKE LOWER(CONCAT('%', :term, '%'))")
    public Page<LocationEntity> search(Pageable pageable, @Param("term") String term);

    public Page<LocationEntity> findByType(LocationType type, Pageable pageable);

    @Transactional(readOnly = true)
    @Query("SELECT l FROM LocationEntity l WHERE l.active = true")
    public Page<LocationEntity> findAllActive(Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE LocationEntity l SET l.active = false WHERE l.id = :id")
    public void deactivate(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE LocationEntity l SET l.active = true WHERE l.id = :id")
    public void activate(@Param("id") Long id);

    @Transactional(readOnly = true)
    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END FROM LocationEntity l WHERE l.code.value = :code")
    boolean existsByCode(@Param("code") String code);

    @Transactional(readOnly = true)
    @Query("""
    SELECT l FROM LocationEntity l
    WHERE (:type IS NULL OR l.type = :type)
    AND (:onlyActive IS NULL OR l.active = :onlyActive)
    """)
    Page<LocationEntity> findAll(
            @Param("type") LocationType type,
            @Param("onlyActive") Boolean onlyActive,
            Pageable pageable);

}
