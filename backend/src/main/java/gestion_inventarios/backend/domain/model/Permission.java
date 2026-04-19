package gestion_inventarios.backend.domain.model;

import lombok.Getter;

@Getter
public class Permission {

    private Long id;
    private String name;

    public Permission(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Permission(String name) {
        this.name = name;
    }
}
