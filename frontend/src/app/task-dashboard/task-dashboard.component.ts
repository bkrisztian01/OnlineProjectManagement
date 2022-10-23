import { Component, OnInit } from '@angular/core';
import { FormBuilder,FormGroup } from '@angular/forms';
import { Task } from '../model/tasks.model';
import { ApiService } from '../services/api.service';
import { Milestone } from '../model/milestones.model';
// import { TaskObjects } from './task-dashboard.model';

@Component({
  selector: 'app-task-dashboard',
  templateUrl: './task-dashboard.component.html',
  styleUrls: ['./task-dashboard.component.css']
})
export class TaskDashboardComponent implements OnInit {

  formValue !: FormGroup;
  milestoneValue !: FormGroup;
  taskObject : Task = new Task();
  taskData !: any;
  showAdd!: boolean;
  showUpdate!: boolean;
  TaskPageNumber:any = 1;
  MilestonePageNumber = 1;


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
      description: [''],
      day : [''],
      statusbar : [''],
      tasks:['']
    })
    this.getAllMilestones();
    // this.getAllProjects();
  }

  setTaskPageNumber(x:any ) {
    this.TaskPageNumber=x;
    this.getAllTasks;
  }
  setMilestonePageNumber(x:any ) {
    this.MilestonePageNumber=x;
    this.getAllTasks;
  }

  Archive: boolean=false;
  setToArchive(){
    this.Archive=true;
  }
  setToActive(){
    this.Archive=false;
  }

  postTaskDeatails(){
    // this.taskObject.name = this.formValue.value.text;
    // this.taskObject.status = this.formValue.value.statusbar;
    // this.taskObject.deadline = this.formValue.value.day;
    // this.taskObject.assignees = this.formValue.value.workers;
    // this.taskObject.description = this.formValue.value.description;
    // this.taskObject.prerequisiteTaskIds = this.formValue.value.prerequisite;

    const reqBody = {
      projectId: 1,
      name: this.formValue.value.text,
      description: this.formValue.value.description,
      deadline: this.formValue.value.day,
    }

    this.api.postTask(reqBody).subscribe(res=>{
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
  onEdit(row:any){
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
    // this.taskObject.text = this.formValue.value.text;
    // this.taskObject.statusbar = this.formValue.value.statusbar;
    // this.taskObject.day = this.formValue.value.day;
    // this.taskObject.workers = this.formValue.value.workers;
    // this.taskObject.description = this.formValue.value.description;
    // this.taskObject.prerequisite = this.formValue.value.prerequisite;

    const reqBody = {
      name: this.formValue.value.text,
      description: this.formValue.value.description,
      deadline: this.formValue.value.day,
      // status: ,
      assigneeIds: [],
      prerequisiteTaskIds: [],
    }
    this.api.updateTask(reqBody,this.taskObject.id).subscribe(
      res=>{
        alert("Update succesful!");
        let ref = document.getElementById('close');
        ref?.click();
        this.formValue.reset();
        this.getAllTasks();
      }
    
    )
  }

  //MILESTONES//

  milestoneObject : Milestone = new Milestone();
  milestoneData !: any;
  showAddMilestone!: boolean;
  showUpdateMilestone!: boolean;


  
  postMilestoneDetails(){
    //this.milestoneObject.name = this.milestoneValue.value.text;
    //this.milestoneObject.status = this.milestoneValue.value.statusbar;
    //this.milestoneObject.deadline = this.milestoneValue.value.day;
    //this.milestoneObject.description = this.milestoneValue.value.description;
    //this.milestoneObject.tasks = this.milestoneValue.value.tasks;
    const reqBody = {
      projectId: 1,
      name: this.milestoneValue.value.text,
      description: this.milestoneValue.value.description,
      deadline: this.milestoneValue.value.day,
    }

    this.api.postMilestone(reqBody).subscribe(res=>{
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
      this.milestoneValue.controls['text'].setValue(row.name);
      this.milestoneValue.controls['statusbar'].setValue(row.status);
      this.milestoneValue.controls['day'].setValue(row.deadline);
      this.milestoneValue.controls['description'].setValue(row.description);
      //this.milestoneValue.controls['tasks'].setValue(row.tasks);
    }

    clickAddMilestone(){
      this.milestoneValue.reset();
        this.showAddMilestone=true;
        this.showUpdateMilestone=false;
    }

    updateMilestone(){
      // this.milestoneObject.name = this.milestoneValue.value.text;
      // this.milestoneObject.status = this.milestoneValue.value.statusbar;
      // this.milestoneObject.deadline = this.milestoneValue.value.day;
      // this.milestoneObject.description = this.milestoneValue.value.description;
      //this.milestoneObject.tasks = this.formValue.value.tasks;
      const reqBody = {
        name: this.milestoneValue.value.text,
        description: this.milestoneValue.value.description,
        deadline: this.milestoneValue.value.day,
        // status: ,
        //assigneeIds: [],
        //prerequisiteTaskIds: [],
      }
      this.api.updateMilestone(reqBody,this.milestoneObject.id).subscribe(
        res=>{
          alert("Update succesful!");
          let ref = document.getElementById('close');
          ref?.click();
          this.milestoneValue.reset();
          this.getAllMilestones();
        }
      
      )
    }
    // projectData!: any;
    // getAllProjects(){
    //   this.api.getProjectData().subscribe(res=>{

    //     this.projectData = res
    //     console.log(this.projectData);
    //   })
    // }

    // projectId: any = 1;
    // setProjectId(newId: any){
    //   this.projectId=newId;
    // }

}
