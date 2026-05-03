export enum DocumentType {
    CC, CE, PASSPORT, NIT, RUT
}

export interface DocumentIdentity {
  documentType: DocumentType;
  documentNumber: string;
}


