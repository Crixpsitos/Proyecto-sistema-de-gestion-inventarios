import { Product as ProductModel } from "../Product/Product";
import { LocationModel } from "../location/LocationModel";
import { MovementType } from "./MovementType";

export interface MovementModel {
    id: number;
    type: MovementType;
    product: ProductModel;
    source: LocationModel | null;
    destination: LocationModel | null;
    quantity: number;
    notes: string;
    createdAt: string;
    createdBy: string;
}
