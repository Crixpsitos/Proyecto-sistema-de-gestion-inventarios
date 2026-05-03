import { ChangeDetectionStrategy, Component, inject, input, output } from '@angular/core';
import { MatFormField, MatFormFieldModule } from "@angular/material/form-field";
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { debounceTime, distinctUntilChanged, switchMap } from 'rxjs';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog } from '@angular/material/dialog';
import { CreateCategoryDialog } from '../create-category-dialog/create-category-dialog';


@Component({
  selector: 'app-toolbar-categorie',
  imports: [MatFormField, MatFormFieldModule, MatIconModule, MatInputModule, MatButtonModule, ReactiveFormsModule],
  templateUrl: './toolbar-categorie.html',
  styleUrl: './toolbar-categorie.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ToolbarCategorie {

  readonly search = new FormControl('');
  readonly canManage = input(false);
  readonly onSearch = output<string>();
  readonly addCategory = output<{name: string, description: string}>();


  readonly dialog = inject(MatDialog);


  public openDialog(): void {
    const dialogRef = this.dialog.open(CreateCategoryDialog);

    dialogRef.afterClosed().subscribe(result => {
      if(result !== undefined) {
        this.addCategory.emit({name: result.name, description: result.description});
      }
    })
  }



   ngOnInit() {
    this.search.valueChanges.pipe(
      debounceTime(400),
      distinctUntilChanged(),
    ).subscribe(value => this.onSearch.emit(value ?? ''));
  }


}
