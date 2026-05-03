import { LocationType } from "./LocationRequest";

export interface LocationModel {
    id: number;
    name: string;
    code: string;
    type: LocationType;
    address: string;
    movementCount: number;
    currentStock: number;
    active: boolean;
    createdAt: string;
    updatedAt: string;
}
