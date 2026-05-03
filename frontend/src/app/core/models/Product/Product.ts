import { Category } from "../category/Category";
import type { ProductPrice } from "./ProductPrice";

export interface Product {
    id: string;
    sku: string;
    name: string;
    description: string;
    category: Category;
    brand: string;
    model: string;
    color: string;
    size: string;
    image: string;
    quantity: number;
    price: ProductPrice["price"];
    currency: ProductPrice["currency"];
    active: boolean;
    createdAt: Date;
    updatedAt: Date;
}
