import type { ProductPrice } from "./ProductPrice";

export interface Product {
    id: string;
    sku: string;
    name: string;
    description: string;
    brand: string;
    model: string;
    color: string;
    size: string;
    image: string;
    quantity: number;
    productPrice: ProductPrice;
    active: boolean;
    createdAt: Date;
    updatedAt: Date;
}
