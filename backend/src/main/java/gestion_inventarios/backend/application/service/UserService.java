package gestion_inventarios.backend.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import gestion_inventarios.backend.application.ports.in.FindUserCase;
import gestion_inventarios.backend.application.ports.out.UserRepositoryPort;
import gestion_inventarios.backend.domain.exception.UserNotFoundException;
import gestion_inventarios.backend.domain.model.User;

@Service
public class UserService implements FindUserCase {

    private final UserRepositoryPort userRepositoryPort;

    public UserService(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
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
}

