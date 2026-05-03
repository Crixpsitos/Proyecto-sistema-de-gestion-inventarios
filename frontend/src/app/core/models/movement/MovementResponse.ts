import { MovementModel } from "./MovementModel";

export interface MovementResponse {
    content: MovementModel[];
    totalElements: number;
    page: number;
    size: number;
    totalPages: number;

}
