package gestion_inventarios.backend.infrastructure.out.persistence.users.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import gestion_inventarios.backend.infrastructure.out.persistence.users.entity.RoleEntity;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByName(String name);
}
