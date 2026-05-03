import { MovementModel } from "@/core/models/movement/MovementModel";

export interface MovementResponse {
    content: MovementModel[];
    totalElements: number;
    page: number;
    size: number;
    totalPages: number;

}
