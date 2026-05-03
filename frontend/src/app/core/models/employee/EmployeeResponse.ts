import { EmployeeModel } from '@/core/models/employee/EmployeeModel';

export interface EmployeeResponse {
  content: EmployeeModel[];
  totalElements: number;
  page: number;
  size: number;
  totalPages: number;
}
