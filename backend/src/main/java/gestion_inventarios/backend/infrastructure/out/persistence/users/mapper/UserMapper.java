package gestion_inventarios.backend.infrastructure.out.persistence.users.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import gestion_inventarios.backend.domain.model.DocumentIdentity;
import gestion_inventarios.backend.domain.model.Permission;
import gestion_inventarios.backend.domain.model.Role;
import gestion_inventarios.backend.domain.model.User;
import gestion_inventarios.backend.infrastructure.out.persistence.users.entity.DocumentIdentityEmbeddable;
import gestion_inventarios.backend.infrastructure.out.persistence.users.entity.RoleEntity;
import gestion_inventarios.backend.infrastructure.out.persistence.users.entity.UserEntity;

@Component
public class UserMapper {

    public User toDomain(UserEntity entity) {
        Role role = toDomainRole(entity.getRole());

        Set<Permission> extraPermissions = entity.getUserPermissions().stream()
            .filter(up -> "override".equalsIgnoreCase(up.getType()))
            .map(up -> new Permission(up.getPermission().getId(), up.getPermission().getName()))
            .collect(Collectors.toSet());

        Set<Permission> deniedPermissions = entity.getUserPermissions().stream()
            .filter(up -> "deny".equalsIgnoreCase(up.getType()))
            .map(up -> new Permission(up.getPermission().getId(), up.getPermission().getName()))
            .collect(Collectors.toSet());

        DocumentIdentity documentIdentity = null;
        if (entity.getDocumentIdentity() != null) {
            documentIdentity = new DocumentIdentity(
                entity.getDocumentIdentity().getDocumentType(),
                entity.getDocumentIdentity().getDocumentNumber()
            );
        }

        return new User(
            entity.getId(),
            entity.getName(),
            entity.getLastName(),
            entity.getEmail(),
            entity.getPassword(),
            entity.getPhone(),
            documentIdentity,
            entity.isEnabled(),
            role,
            extraPermissions,
            deniedPermissions,
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

  
    public UserEntity toEntity(User user, RoleEntity roleEntity) {
        DocumentIdentityEmbeddable docEmbeddable = toDocumentEmbeddable(user);

        return new UserEntity(
            user.getName(),
            user.getLastName(),
            user.getEmail(),
            user.getPassword(),
            user.getPhone(),
            docEmbeddable,
            roleEntity
        );
    }

    public void syncEntity(UserEntity entity, User user, RoleEntity roleEntity) {
        DocumentIdentityEmbeddable docEmbeddable = toDocumentEmbeddable(user);
        entity.syncState(
            user.getName(),
            user.getLastName(),
            user.getEmail(),
            user.getPassword(),
            user.getPhone(),
            docEmbeddable,
            user.isEnabled(),
            roleEntity
        );
    }

    private DocumentIdentityEmbeddable toDocumentEmbeddable(User user) {
        if (user.getDocumentIdentity() == null) {
            return null;
        }

        return new DocumentIdentityEmbeddable(
            user.getDocumentIdentity().getDocumentType(),
            user.getDocumentIdentity().getDocumentNumber()
        );
    }

    private Role toDomainRole(RoleEntity entity) {
        Set<Permission> permissions = entity.getPermissions().stream()
            .map(p -> new Permission(p.getId(), p.getName()))
            .collect(Collectors.toSet());
        return new Role(entity.getId(), entity.getName(), permissions);
    }
}