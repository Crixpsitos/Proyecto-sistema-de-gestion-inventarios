package gestion_inventarios.backend.infrastructure.out.persistence.users.entity;

import gestion_inventarios.backend.domain.model.user.DocumentType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class DocumentIdentityEmbeddable {

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", nullable = false)
    private DocumentType documentType;

    @Column(name = "document_number", nullable = false)
    private String documentNumber;

    public DocumentIdentityEmbeddable(DocumentType documentType, String documentNumber) {
        this.documentType = documentType;
        this.documentNumber = documentNumber;
    }
}
