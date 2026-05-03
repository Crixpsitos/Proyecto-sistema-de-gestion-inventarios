import type { SelectItem } from "@/feature/shared/interfaces/SelectItem";

export type MapperFn<T> = (item: T) => SelectItem;
