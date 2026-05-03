package gestion_inventarios.backend.domain.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import lombok.Getter;

@Getter
public class User {

    private Long id;
    private String name;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private DocumentIdentity documentIdentity;
    private boolean enabled;
    private Role role;
    private Set<Permission> extraPermissions;
    private Set<Permission> deniedPermissions;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User(Long id, String name, String lastName, String email, String password,
                String phone, DocumentIdentity documentIdentity, boolean enabled,
                Role role, Set<Permission> extraPermissions, Set<Permission> deniedPermissions,
                LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.documentIdentity = documentIdentity;
        this.enabled = enabled;
        this.role = role;
        this.extraPermissions = extraPermissions;
        this.deniedPermissions = deniedPermissions;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public User(String name, String lastName, String email, String password,
                String phone, DocumentIdentity documentIdentity, Role role) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.documentIdentity = documentIdentity;
        this.enabled = true;
        this.role = role;
        this.extraPermissions = Set.of();
        this.deniedPermissions = Set.of();
    }

    public void updateProfile(String name, String lastName, String email, String phone, DocumentIdentity documentIdentity) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.documentIdentity = documentIdentity;
    }

    public void updateRole(Role role) {
        this.role = role;
    }

    public void updatePermissions(Set<Permission> extraPermissions, Set<Permission> deniedPermissions) {
        this.extraPermissions = extraPermissions == null ? Set.of() : new HashSet<>(extraPermissions);
        this.deniedPermissions = deniedPermissions == null ? Set.of() : new HashSet<>(deniedPermissions);
    }

    public void activate() {
        this.enabled = true;
    }

    public void deactivate() {
        this.enabled = false;
    }

    public void updatePassword(String passwordHash) {
        this.password = passwordHash;
    }
}

