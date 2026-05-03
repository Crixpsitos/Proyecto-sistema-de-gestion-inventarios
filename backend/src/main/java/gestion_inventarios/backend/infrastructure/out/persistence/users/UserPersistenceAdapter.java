package gestion_inventarios.backend.infrastructure.out.persistence.users;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import gestion_inventarios.backend.application.ports.out.UserRepositoryPort;
import gestion_inventarios.backend.domain.model.User;
import gestion_inventarios.backend.domain.model.shared.PageRequest;
import gestion_inventarios.backend.domain.model.shared.PageResult;
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
        return userRepository.findByEmail(email)
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
    @Transactional(readOnly = true)
    public PageResult<User> search(PageRequest pageRequest, String search) {
        org.springframework.data.domain.PageRequest springPage = org.springframework.data.domain.PageRequest
            .of(pageRequest.page(), pageRequest.size(), Sort.by(Sort.Direction.ASC, "createdAt"));

        var page = userRepository.search(springPage, search == null ? "" : search.trim());

        List<User> content = page.getContent().stream()
            .map(userMapper::toDomain)
            .toList();

        return PageResult.of(content, pageRequest.page(), pageRequest.size(), page.getTotalElements());
    }

    @Override
    @Transactional
    public User save(User user) {
        RoleEntity roleEntity = roleRepository.findByName(user.getRole().getName())
            .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado: " + user.getRole().getName()));

        UserEntity entity;
        if (user.getId() == null) {
            entity = userMapper.toEntity(user, roleEntity);
            entity = userRepository.save(entity);
        } else {
            entity = userRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con id: " + user.getId()));
            userMapper.syncEntity(entity, user, roleEntity);
            entity = userRepository.save(entity);
        }

        return userMapper.toDomain(entity);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}

