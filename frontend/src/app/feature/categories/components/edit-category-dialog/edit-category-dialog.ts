import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogClose,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle,
} from '@angular/material/dialog';
import { Category } from '@/core/models/category/Category';

@Component({
  selector: 'app-edit-category-dialog',
  imports: [
    MatFormFieldModule,
    MatInputModule,
    FormsModule,
    MatButtonModule,
    MatDialogTitle,
    MatDialogContent,
    MatDialogActions,
    MatDialogClose,
  ],
  templateUrl: './edit-category-dialog.html',
  styleUrl: './edit-category-dialog.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class EditCategoryDialog {

  readonly dialogRef = inject(MatDialogRef<EditCategoryDialog>);
  readonly data: Category = { ...inject<Category>(MAT_DIALOG_DATA) };

  onNoClick(): void {
    this.dialogRef.close();
  }

}
