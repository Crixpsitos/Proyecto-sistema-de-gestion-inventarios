import { Permissions } from "../permissions/Permissions";

export interface Role {
  id : number;
  name : string;
  permissions: Permissions[];
}
