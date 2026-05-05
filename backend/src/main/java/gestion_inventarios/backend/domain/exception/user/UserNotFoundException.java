package gestion_inventarios.backend.domain.exception.user;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id) {
        super("Usuario no encontrado con id: " + id);
    }

    public UserNotFoundException(String email) {
        super("Usuario no encontrado con email: " + email);
    }
}
