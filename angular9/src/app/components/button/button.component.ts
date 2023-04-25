import { Component,Input,Output,EventEmitter } from '@angular/core';

@Component({
  selector: 'app-button',
  templateUrl: './button.component.html',
  styleUrls: ['./button.component.css']
})
export class ButtonComponent {
  @Input() text: string ="Submit";
  @Input() color: string="green";
  @Output() btnClick = new EventEmitter();

  constructor() {
  }
  onClick()
  {
    console.log("button clicked");
    this.btnClick.emit();
  }
}
