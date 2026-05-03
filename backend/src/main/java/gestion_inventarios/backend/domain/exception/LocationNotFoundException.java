package gestion_inventarios.backend.domain.exception;

public class LocationNotFoundException extends RuntimeException {
    public LocationNotFoundException(Long id) {
        super("Locacion con id " + id + " no encontrado");
    }
}