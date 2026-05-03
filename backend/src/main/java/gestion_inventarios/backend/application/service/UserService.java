package gestion_inventarios.backend.application.service;

import java.util.List;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import gestion_inventarios.backend.application.ports.in.EmployeeUseCase;
import gestion_inventarios.backend.application.ports.in.FindUserCase;
import gestion_inventarios.backend.application.ports.out.UserRepositoryPort;
import gestion_inventarios.backend.domain.exception.UserAlreadyExistsException;
import gestion_inventarios.backend.domain.exception.UserNotFoundException;
import gestion_inventarios.backend.domain.model.DocumentIdentity;
import gestion_inventarios.backend.domain.model.DocumentType;
import gestion_inventarios.backend.domain.model.Role;
import gestion_inventarios.backend.domain.model.User;
import gestion_inventarios.backend.domain.model.UserAuthDTO;
import gestion_inventarios.backend.domain.model.shared.PageRequest;
import gestion_inventarios.backend.domain.model.shared.PageResult;

@Service
public class UserService implements FindUserCase, EmployeeUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepositoryPort userRepositoryPort, PasswordEncoder passwordEncoder) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User findByEmail(String email) {
        return userRepositoryPort.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException(email));
    }

    @Override
    public User findById(Long id) {
        return userRepositoryPort.findById(id)
            .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public List<User> findAll() {
        return userRepositoryPort.findAll();
    }

    @Override
    public UserAuthDTO findByEmailForAuth(String email) {
        User user = findByEmail(email);
        return new UserAuthDTO(user.getId(), user.getEmail(), user.getPassword(), user.getRole());
    }

    @Override
    public PageResult<User> search(PageRequest pageRequest, String search) {
        return userRepositoryPort.search(pageRequest, search == null ? "" : search.trim());
    }

    @Override
    public User create(
        String name,
        String lastName,
        String email,
        String rawPassword,
        String phone,
        DocumentType documentType,
        String documentNumber,
        String roleName
    ) {
        userRepositoryPort.findByEmail(email).ifPresent(existing -> {
            throw new UserAlreadyExistsException(email);
        });

        DocumentIdentity documentIdentity = new DocumentIdentity(documentType, documentNumber);
        Role role = new Role(roleName, Set.of());

        User user = new User(
            name,
            lastName,
            email,
            passwordEncoder.encode(rawPassword),
            phone,
            documentIdentity,
            role
        );

        return userRepositoryPort.save(user);
    }

    @Override
    public User updateProfile(
        Long id,
        String name,
        String lastName,
        String email,
        String phone,
        DocumentType documentType,
        String documentNumber
    ) {
        User user = findById(id);
        if (!user.getEmail().equalsIgnoreCase(email)) {
            userRepositoryPort.findByEmail(email).ifPresent(existing -> {
                throw new UserAlreadyExistsException(email);
            });
        }

        user.updateProfile(name, lastName, email, phone, new DocumentIdentity(documentType, documentNumber));
        return userRepositoryPort.save(user);
    }

    @Override
    public User updateRoleAndPermissions(Long id, String roleName) {
        User user = findById(id);
        user.updateRole(new Role(roleName, Set.of()));
        user.updatePermissions(Set.of(), Set.of());
        return userRepositoryPort.save(user);
    }

    @Override
    public User activate(Long id) {
        User user = findById(id);
        user.activate();
        return userRepositoryPort.save(user);
    }

    @Override
    public User deactivate(Long id) {
        User user = findById(id);
        user.deactivate();
        return userRepositoryPort.save(user);
    }

}

