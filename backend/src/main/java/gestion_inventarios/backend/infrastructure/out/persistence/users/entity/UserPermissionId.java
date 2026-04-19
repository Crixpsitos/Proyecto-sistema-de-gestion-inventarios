package gestion_inventarios.backend.infrastructure.out.persistence.users.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class UserPermissionId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "permission_id")
    private Long permissionId;

    public UserPermissionId(Long userId, Long permissionId) {
        this.userId = userId;
        this.permissionId = permissionId;
    }
}
