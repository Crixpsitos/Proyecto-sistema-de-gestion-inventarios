import { Permissions } from "../permissions/Permissions";
import { Role } from "../role/Role";
import { DocumentIdentity } from "./DocumentIndentity";

export interface User {
  email: string;
  name: string;
  lastName: string;
  phone: string;
  role: Role;
  extraPermissions: Permissions[];
  deniedPermissions: Permissions[];
  enabled: boolean;
  documentIdentity: DocumentIdentity;
  createdAt: Date;
  updatedAt: Date;
}
