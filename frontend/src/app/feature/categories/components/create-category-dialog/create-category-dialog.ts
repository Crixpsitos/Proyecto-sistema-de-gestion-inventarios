import { ChangeDetectionStrategy, Component, inject, model } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import {
  MAT_DIALOG_DATA,
  MatDialog,
  MatDialogActions,
  MatDialogClose,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle,
} from '@angular/material/dialog';

interface DialogData {
  name : string;
  description : string;
}

@Component({
  selector: 'app-create-category-dialog',
  imports: [ MatFormFieldModule,
    MatInputModule,
    FormsModule,
    MatButtonModule,
    MatDialogTitle,
    MatDialogContent,
    MatDialogActions,
    MatDialogClose,],
  templateUrl: './create-category-dialog.html',
  styleUrl: './create-category-dialog.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CreateCategoryDialog {


  readonly dialogRef = inject(MatDialogRef<CreateCategoryDialog>);
  readonly data: DialogData = inject<DialogData>(MAT_DIALOG_DATA) ?? { name: '', description: '' };

  onNoClick(): void {
    this.dialogRef.close();

  }

}
