package gestion_inventarios.backend.domain.exception;

public class MovementNotFoundException extends RuntimeException {
    public MovementNotFoundException(Long id) {
        super("Movimiento con id " + id + " no encontrado");
    }
    
}
