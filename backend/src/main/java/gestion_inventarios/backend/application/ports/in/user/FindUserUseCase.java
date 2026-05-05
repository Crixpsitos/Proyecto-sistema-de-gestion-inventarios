package gestion_inventarios.backend.application.ports.in.user;

import java.util.List;

import gestion_inventarios.backend.domain.model.user.User;
import gestion_inventarios.backend.domain.model.user.UserAuthDTO;

public interface FindUserUseCase {
    User findByEmail(String email);
    UserAuthDTO findByEmailForAuth(String email);
    User findById(Long id);
    List<User> findAll();
}

