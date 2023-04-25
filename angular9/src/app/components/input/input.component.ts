import {Component, EventEmitter, Input, Output} from '@angular/core';
import {FormControl, Validators} from "@angular/forms";

@Component({
  selector: 'app-input',
  templateUrl: './input.component.html',
  styleUrls: ['./input.component.css']
})
export class InputComponent {
  @Output() newValueEvent = new EventEmitter<string>();
  @Input() placeholderText: string = "";
  @Input() labelText: string = "";
  inputFormControl = new FormControl("",[Validators.required])
  constructor() {
  }
  onChange()
  {
    if(this.inputFormControl.value)
      this.newValueEvent.emit(this.inputFormControl.value)
  }
}
