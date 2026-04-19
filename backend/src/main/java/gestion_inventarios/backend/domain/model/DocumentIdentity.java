package gestion_inventarios.backend.domain.model;

import lombok.Value;

@Value
public class DocumentIdentity {
    

    private DocumentType documentType;
    private String documentNumber;

    public DocumentIdentity(DocumentType documentType, String documentNumber) {
        if (documentType == null) {
            throw new IllegalArgumentException("El tipo de documento no puede ser nulo");
        }

        if (documentNumber == null) {
            throw new IllegalArgumentException("El número de documento no puede ser nulo");
        }

        this.documentType = documentType;
        this.documentNumber = sanitize(documentNumber);
        validateRules();
    }


    private String sanitize(String raw) {
        return raw.replaceAll("[^a-zA-Z0-9]", "").toUpperCase();
    }

    private void validateRules() {
        if (this.documentType == DocumentType.CC && !this.documentNumber.matches("\\d{6,11}")) {
            throw new IllegalArgumentException("La Cédula debe tener entre 6 y 11 dígitos numéricos.");
        }
        if (this.documentType == DocumentType.NIT && !this.documentNumber.matches("\\d{8,15}")) {
            throw new IllegalArgumentException("El NIT tiene un formato inválido.");
        }
    }

    public String getFormattedDocument() {
        return documentType.name() + "-" + documentNumber;
    }
}
