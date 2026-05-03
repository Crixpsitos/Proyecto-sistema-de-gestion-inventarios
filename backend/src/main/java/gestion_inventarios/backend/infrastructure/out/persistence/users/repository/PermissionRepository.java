package gestion_inventarios.backend.infrastructure.out.persistence.users.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import gestion_inventarios.backend.infrastructure.out.persistence.users.entity.PermissionEntity;

public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {
    Optional<PermissionEntity> findByName(String name);
    List<PermissionEntity> findByNameIn(List<String> names);
}
