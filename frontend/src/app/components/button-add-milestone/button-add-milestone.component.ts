import { Component, Input, OnInit, Output, EventEmitter} from '@angular/core';

@Component({
  selector: 'app-button-add-milestone',
  templateUrl: './button-add-milestone.component.html',
  styleUrls: ['./button-add-milestone.component.css']
})
export class ButtonAddMilestoneComponent implements OnInit {

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
