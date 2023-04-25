import {Component, Input, Output, EventEmitter } from '@angular/core';
import {FormControl, FormGroupDirective, NgForm, Validators} from '@angular/forms';
import {ErrorStateMatcher} from '@angular/material/core';

/** Error when invalid control is dirty, touched, or submitted. */
export class MyErrorStateMatcher implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    const isSubmitted = form && form.submitted;
    return !!(control && control.invalid && (control.dirty || control.touched || isSubmitted));
  }
}
export interface IMultiSelectItem {
  viewValue: string;
  value: string;
}

@Component({
  selector: 'app-multiselect',
  templateUrl: './multiselect.component.html',
  styleUrls: ['./multiselect.component.css']
})
export class MultiselectComponent {

  @Input() itemList:IMultiSelectItem[]=[];
  @Input() multiselectable: boolean = true;
  @Output() handleSubmit = new EventEmitter<string[]>();
  selected = new FormControl([], [Validators.required]);

  selectFormControl = new FormControl('valid', [Validators.required]);
  matcher = new MyErrorStateMatcher();

  constructor() {}

  getSelectedItems()
  {
    if(this.selected.value) {
      this.handleSubmit.emit(this.selected.value);
      //remove from selection
      if(this.multiselectable)
        this.selected.setValue([]);
    }
  }
}
