import { Component, OnInit } from '@angular/core';
import { FormBuilder,FormGroup } from '@angular/forms';
import { ApiService } from '../services/api.service';
import { TaskObjects } from './task-dashboard.model';

@Component({
  selector: 'app-task-dashboard',
  templateUrl: './task-dashboard.component.html',
  styleUrls: ['./task-dashboard.component.css']
})
export class TaskDashboardComponent implements OnInit {

  formValue !: FormGroup;
  milestoneValue !: FormGroup;
  taskObject : TaskObjects = new TaskObjects();
  taskData !: any;
  showAdd!: boolean;
  showUpdate!: boolean;
  constructor(private formbuilder: FormBuilder, private api: ApiService){}

  ngOnInit():void{
    this.formValue = this.formbuilder.group({
      text : [''],
      statusbar : [''],
      day : [''],
      workers : [''],
      description: [''],
      prerequisite:['']
    })
    this.getAllTasks();

    this.milestoneValue = this.formbuilder.group({
      text : [''],
      statusbar : [''],
      day : [''],
      workers : [''],
      description: [''],
      prerequisite:['']
    })
    this.getAllMilestones();
  }
  postTaskDetails(){
    this.taskObject.text = this.formValue.value.text;
    this.taskObject.statusbar = this.formValue.value.statusbar;
    this.taskObject.day = this.formValue.value.day;
    this.taskObject.workers = this.formValue.value.workers;
    this.taskObject.description = this.formValue.value.description;
    this.taskObject.prerequisite = this.formValue.value.prerequisite;

    this.api.postTask(this.taskObject).subscribe(res=>{
        console.log(res);
        alert("Task added succesfully!");
        let ref = document.getElementById('close');
        ref?.click();
        this.formValue.reset();
        this.getAllTasks();
    },
    err=>{
      alert("Something went wrong!")
    }
    )}
    getAllTasks(){
      this.api.getTask().subscribe(res=>{
        this.taskData = res
      })
    }
    deleteTask(row: any){
      this.api.deleteTask(row.id).subscribe(
        res=>{
          alert("Task deleted");
          this.getAllTasks();
        }
      )
    }
    onEditTask(row:any){
      this.showAdd=false;
      this.showUpdate=true;
      this.taskObject.id = row.id;
      this.formValue.controls['text'].setValue(row.text);
      this.formValue.controls['statusbar'].setValue(row.statusbar);
      this.formValue.controls['day'].setValue(row.day);
      this.formValue.controls['workers'].setValue(row.workers);
      this.formValue.controls['description'].setValue(row.description);
      this.formValue.controls['prerequisite'].setValue(row.prerequisite);
    }

    clickAddTask(){
      this.formValue.reset();
        this.showAdd=true;
        this.showUpdate=false;
    }

    updateTask(){
      this.taskObject.text = this.formValue.value.text;
      this.taskObject.statusbar = this.formValue.value.statusbar;
      this.taskObject.day = this.formValue.value.day;
      this.taskObject.workers = this.formValue.value.workers;
      this.taskObject.description = this.formValue.value.description;
      this.taskObject.prerequisite = this.formValue.value.prerequisite;
      this.api.updateTask(this.taskObject,this.taskObject.id).subscribe(
        res=>{
          alert("Update succesful!");
          let ref = document.getElementById('close');
          ref?.click();
          this.formValue.reset();
          this.getAllTasks();
        }
      
      )
    }

  milestoneObject : TaskObjects = new TaskObjects();
  milestoneData !: any;
  showAddMilestone!: boolean;
  showUpdateMilestone!: boolean;


  
  postMilestoneDetails(){
    this.milestoneObject.text = this.milestoneValue.value.text;
    this.milestoneObject.statusbar = this.milestoneValue.value.statusbar;
    this.milestoneObject.day = this.milestoneValue.value.day;
    this.milestoneObject.description = this.milestoneValue.value.description;
    this.milestoneObject.prerequisite = this.milestoneValue.value.prerequisite;

    this.api.postMilestone(this.milestoneObject).subscribe(res=>{
        console.log(res);
        alert("Milestone added succesfully!");
        let ref = document.getElementById('close');
        ref?.click();
        this.milestoneValue.reset();
        this.getAllMilestones();
    },
    err=>{
      alert("Something went wrong!")
    }
    )}
    getAllMilestones(){
      this.api.getMilestone().subscribe(res=>{
        this.milestoneData = res
      })
    }
    deleteMilestone(row: any){
      this.api.deleteMilestone(row.id).subscribe(
        res=>{
          alert("Milestone deleted");
          this.getAllMilestones();
        }
      )
    }
    onEditMilestone(row:any){
      this.showAddMilestone=false;
      this.showUpdateMilestone=true;
      this.milestoneObject.id = row.id;
      this.milestoneValue.controls['text'].setValue(row.text);
      this.milestoneValue.controls['statusbar'].setValue(row.statusbar);
      this.milestoneValue.controls['day'].setValue(row.day);
      this.milestoneValue.controls['description'].setValue(row.description);
      this.milestoneValue.controls['prerequisite'].setValue(row.prerequisite);
    }

    clickAddMilestone(){
      this.milestoneValue.reset();
        this.showAddMilestone=true;
        this.showUpdateMilestone=false;
    }

    updateMilestone(){
      this.milestoneObject.text = this.milestoneValue.value.text;
      this.milestoneObject.statusbar = this.milestoneValue.value.statusbar;
      this.milestoneObject.day = this.milestoneValue.value.day;
      this.milestoneObject.description = this.milestoneValue.value.description;
      this.milestoneObject.prerequisite = this.formValue.value.prerequisite;
      this.api.updateMilestone(this.milestoneObject,this.milestoneObject.id).subscribe(
        res=>{
          alert("Update succesful!");
          let ref = document.getElementById('close');
          ref?.click();
          this.milestoneValue.reset();
          this.getAllMilestones();
        }
      
      )
    }

}
