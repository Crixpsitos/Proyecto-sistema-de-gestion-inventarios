package gestion_inventarios.backend.infrastructure.out.persistence.users;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import gestion_inventarios.backend.application.ports.out.UserRepositoryPort;
import gestion_inventarios.backend.domain.model.User;
import gestion_inventarios.backend.infrastructure.out.persistence.users.entity.RoleEntity;
import gestion_inventarios.backend.infrastructure.out.persistence.users.entity.UserEntity;
import gestion_inventarios.backend.infrastructure.out.persistence.users.mapper.UserMapper;
import gestion_inventarios.backend.infrastructure.out.persistence.users.repository.RoleRepository;
import gestion_inventarios.backend.infrastructure.out.persistence.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserRepositoryPort {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id)
            .map(userMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email))
            .map(userMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll().stream()
            .map(userMapper::toDomain)
            .toList();
    }

    @Override
    public User save(User user) {
        RoleEntity roleEntity = roleRepository.findByName(user.getRole().getName())
            .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado: " + user.getRole().getName()));

        UserEntity entity = userMapper.toEntity(user, roleEntity);
        return userMapper.toDomain(userRepository.save(entity));
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}

