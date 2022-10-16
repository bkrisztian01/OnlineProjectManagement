import { Component, Input, OnInit, Output, EventEmitter} from '@angular/core';

@Component({
  selector: 'app-button-finish',
  templateUrl: './button-finish.component.html',
  styleUrls: ['./button-finish.component.css']
})
export class ButtonFinishComponent implements OnInit {

  @Input() text: string | undefined;
  @Input() color: string | undefined;
  @Output() btnClick = new EventEmitter();

  constructor() { }

  ngOnInit(): void {
  }

  onClick(){
    this.btnClick.emit();
  }
  
}
