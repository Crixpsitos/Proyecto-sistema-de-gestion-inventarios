import type { Observable } from "rxjs";

export type FetchFn<T> = (page: number, pageSize: number) => Observable<T[]>;
