import { Product } from "@/core/models/Product/Product";

export interface ProductResponse {
    content: Product[];
    page: number;
    size: number;
    totalElements: number;
    totalPages: number;
}
