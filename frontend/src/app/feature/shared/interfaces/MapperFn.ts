import type { SelectItem } from "./SelectItem";

export type MapperFn<T> = (item: T) => SelectItem;
