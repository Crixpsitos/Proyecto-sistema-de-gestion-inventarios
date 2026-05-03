export interface User {
  id: number;
  email: string;
  name: string;
  lastName: string;
  phone: string | null;
  role: string;
  permissions: string[];
  createdAt: string;
}
