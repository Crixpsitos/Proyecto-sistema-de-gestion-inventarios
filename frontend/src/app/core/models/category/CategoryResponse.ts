import { Category } from "./Category";

export interface CategoryResponse {
  content: Category[];
  totalElements: number;
  page: number;
  size: number;
  totalPages: number;
}
