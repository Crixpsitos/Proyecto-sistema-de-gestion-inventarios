import { Permissions } from "@/core/models/permissions/Permissions";

export interface Role {
  id : number;
  name : string;
  permissions: Permissions[];
}
