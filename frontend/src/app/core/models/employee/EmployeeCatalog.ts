export interface EmployeeOption {
  code: string;
  label: string;
}

export interface EmployeeCatalog {
  roles: EmployeeOption[];
  permissions: EmployeeOption[];
}
