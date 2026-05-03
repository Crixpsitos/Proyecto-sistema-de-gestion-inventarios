package gestion_inventarios.backend.domain.exception;

public class CategoryDeleteNotAllowedException extends RuntimeException {
    public CategoryDeleteNotAllowedException(Long id) {
        super("No se puede eliminar la categoria con id " + id + " porque tiene productos asociados");
    }
}
