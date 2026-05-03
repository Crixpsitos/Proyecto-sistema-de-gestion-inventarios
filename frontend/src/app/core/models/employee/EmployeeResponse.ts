import { EmployeeModel } from './EmployeeModel';

export interface EmployeeResponse {
  content: EmployeeModel[];
  totalElements: number;
  page: number;
  size: number;
  totalPages: number;
}
