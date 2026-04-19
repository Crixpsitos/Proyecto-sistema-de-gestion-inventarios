package gestion_inventarios.backend.infrastructure.out.persistence.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import gestion_inventarios.backend.infrastructure.out.persistence.users.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query("SELECT u FROM UserEntity u WHERE u.email = :email")
    UserEntity findByEmail(String email);
}
