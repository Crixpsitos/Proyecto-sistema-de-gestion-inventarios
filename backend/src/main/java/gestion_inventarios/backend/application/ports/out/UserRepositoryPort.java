package gestion_inventarios.backend.application.ports.out;

import java.util.List;
import java.util.Optional;

import gestion_inventarios.backend.domain.model.User;
import gestion_inventarios.backend.domain.model.shared.PageRequest;
import gestion_inventarios.backend.domain.model.shared.PageResult;

public interface UserRepositoryPort {
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    List<User> findAll();
    PageResult<User> search(PageRequest pageRequest, String search);
    User save(User user);
    void deleteById(Long id);
}
