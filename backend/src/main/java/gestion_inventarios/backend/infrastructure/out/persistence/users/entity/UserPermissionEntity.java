package gestion_inventarios.backend.infrastructure.out.persistence.users.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_permissions")
@Getter
@NoArgsConstructor
public class UserPermissionEntity {

    @EmbeddedId
    private UserPermissionId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("permissionId")
    @JoinColumn(name = "permission_id")
    private PermissionEntity permission;

    @Column(nullable = false)
    private String type;

    @Column(name = "assigned_at", nullable = false)
    private LocalDateTime assignedAt;

    public UserPermissionEntity(UserEntity user, PermissionEntity permission, String type) {
        this.id = new UserPermissionId(user.getId(), permission.getId());
        this.user = user;
        this.permission = permission;
        this.type = type;
        this.assignedAt = LocalDateTime.now();
    }
}
