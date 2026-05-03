export interface EmployeeModel {
  id: number;
  name: string;
  lastName: string;
  email: string;
  phone: string | null;
  documentType: string;
  documentNumber: string;
  enabled: boolean;
  role: string;
  createdAt: string;
  updatedAt: string;
}
