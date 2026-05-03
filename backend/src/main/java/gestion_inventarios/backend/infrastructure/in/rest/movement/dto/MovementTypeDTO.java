package gestion_inventarios.backend.infrastructure.in.rest.movement.dto;
import com.fasterxml.jackson.annotation.JsonProperty;

public record MovementTypeDTO(
    @JsonProperty("label") String label, 
    @JsonProperty("value") String value
) {
    public static MovementTypeDTO from(String label, String value) {
        return new MovementTypeDTO(label, value);
    }
}
