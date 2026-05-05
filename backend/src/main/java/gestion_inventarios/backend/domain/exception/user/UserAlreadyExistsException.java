package gestion_inventarios.backend.domain.exception.user;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String email) {
        super("Ya existe un usuario con email: " + email);
    }
}
