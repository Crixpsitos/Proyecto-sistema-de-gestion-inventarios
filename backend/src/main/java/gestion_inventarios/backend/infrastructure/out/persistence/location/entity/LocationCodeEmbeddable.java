package gestion_inventarios.backend.infrastructure.out.persistence.location.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class LocationCodeEmbeddable {
    private String value;
    protected LocationCodeEmbeddable() {}
    public LocationCodeEmbeddable(String value) { this.value = value; }
    public String getValue() { return value; }
}
