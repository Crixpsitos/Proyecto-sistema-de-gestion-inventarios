import { Product as ProductModel } from "@/core/models/Product/Product";
import { LocationModel } from "@/core/models/location/LocationModel";
import { MovementType } from "@/core/models/movement/MovementType";

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
