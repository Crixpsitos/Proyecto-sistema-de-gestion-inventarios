import { Product } from "./Product";

export interface ProductResponse {
    content: Product[];
    page: number;
    size: number;
    totalElements: number;
    totalPages: number;
}
