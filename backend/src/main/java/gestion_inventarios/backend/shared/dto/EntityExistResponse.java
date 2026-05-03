package gestion_inventarios.backend.shared.dto;

public record EntityExistResponse(boolean isAvailable) {
    public static EntityExistResponse from(boolean isAvailable) {
        return new EntityExistResponse(isAvailable);
    }
}
