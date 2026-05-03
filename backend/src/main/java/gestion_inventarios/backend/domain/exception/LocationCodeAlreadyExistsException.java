package gestion_inventarios.backend.domain.exception;

public class LocationCodeAlreadyExistsException extends RuntimeException {
    public LocationCodeAlreadyExistsException(String code) {
        super("Locacion con codigo " + code + " ya existe");
    }
}