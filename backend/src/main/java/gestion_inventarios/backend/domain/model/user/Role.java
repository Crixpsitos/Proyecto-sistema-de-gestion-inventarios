package gestion_inventarios.backend.domain.model.user;

import java.util.Set;

import lombok.Getter;

@Getter
public class Role {

    private Long id;
    private String name;
    private Set<Permission> permissions;

    public Role(Long id, String name, Set<Permission> permissions) {
        this.id = id;
        this.name = name;
        this.permissions = permissions;
    }

    public Role(String name, Set<Permission> permissions) {
        this.name = name;
        this.permissions = permissions;
    }

    public boolean hasPermission(String permissionName) {
        return permissions.stream().anyMatch(p -> p.getName().equals(permissionName));
    }
}
