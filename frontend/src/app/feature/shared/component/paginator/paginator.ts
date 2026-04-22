import { Component, input, output } from '@angular/core';
import { MatPaginator, PageEvent } from "@angular/material/paginator";

@Component({
  selector: 'app-paginator',
  imports: [MatPaginator],
  templateUrl: './paginator.html',
  styleUrl: './paginator.css',
})
export class Paginator {

  public pageSizeOptions = [5, 10, 25];
  public pageSize = input<number>(10);
  public currentPage = input<number>(0);
  public totalElements = input<number>(0);
  public pageEvent = output<PageEvent>();

  public handlePageEvent(event: PageEvent) {
    this.pageEvent.emit(event);
  }
}
