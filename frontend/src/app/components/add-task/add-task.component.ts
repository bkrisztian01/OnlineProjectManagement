import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { Task } from 'src/app/Task';
import { UiService } from 'src/app/services/ui.service';
import {Subscription} from 'rxjs';

@Component({
  selector: 'app-add-task',
  templateUrl: './add-task.component.html',
  styleUrls: ['./add-task.component.css']
})
export class AddTaskComponent implements OnInit {
  @Output() onAddTask: EventEmitter<Task> = new EventEmitter();
  text!: string;
  statusbar!: string;
  day!: string;
  workers!: string;
  description!: string;
  prerequisite!: string;
  showAddTask!: boolean;
  subscription!: Subscription;


  constructor(private uiService: UiService) {
    this.subscription = this.uiService.onToggle().subscribe(value => this.showAddTask = value);
   }

  ngOnInit(): void {
  }
  onSubmit(){
    if(!this.text){
      alert('Please add text');
      return;
    }
    const newTask={
      text: this.text,
      statusbar: this.statusbar,
      day: this.day,
      workers: this.workers,
      description: this.description,
      prerequisite: this.prerequisite
    }

    this.onAddTask.emit(newTask);

    this.text = '';
    this.statusbar = '';
    this.day = '';
    this.workers = '';
    this.description = '';
    this.prerequisite = '';

  }

}
