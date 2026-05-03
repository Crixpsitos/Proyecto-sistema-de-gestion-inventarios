package gestion_inventarios.backend.application.ports.in;

import gestion_inventarios.backend.domain.model.DocumentType;
import gestion_inventarios.backend.domain.model.User;
import gestion_inventarios.backend.domain.model.shared.PageRequest;
import gestion_inventarios.backend.domain.model.shared.PageResult;

public interface EmployeeUseCase {
    PageResult<User> search(PageRequest pageRequest, String search);
    User findById(Long id);
    User create(
        String name,
        String lastName,
        String email,
        String rawPassword,
        String phone,
        DocumentType documentType,
        String documentNumber,
        String roleName
    );

    User updateProfile(
        Long id,
        String name,
        String lastName,
        String email,
        String phone,
        DocumentType documentType,
        String documentNumber
    );

    User updateRoleAndPermissions(
        Long id,
        String roleName
    );

    User activate(Long id);
    User deactivate(Long id);
}
