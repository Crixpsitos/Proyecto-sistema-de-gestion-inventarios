package gestion_inventarios.backend.application.ports.in;

import java.util.List;

import gestion_inventarios.backend.domain.model.User;

public interface FindUserCase {
    User findByEmail(String email);
    User findById(Long id);
    List<User> findAll();
}

