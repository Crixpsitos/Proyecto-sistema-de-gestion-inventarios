package gestion_inventarios.backend.domain.model.shared;

public record PageRequest(int page, int size) {
        public PageRequest {
        if (page < 0) throw new IllegalArgumentException("page no puede ser negativo");
        if (size < 1 || size > 100) throw new IllegalArgumentException("size debe estar entre 1 y 100");
    }
}
