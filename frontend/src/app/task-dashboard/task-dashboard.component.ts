import { Component, OnInit } from '@angular/core';
import { FormBuilder,FormGroup } from '@angular/forms';
import { Task } from '../model/tasks.model';
import { ApiService } from '../services/api.service';
import { Milestone } from '../model/milestones.model';
import { User } from '../model/users.model';
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

  AccessToken!: String;

  constructor(private formbuilder: FormBuilder, private api: ApiService){}

  projectId: number = 1;
  projectName: String= "";

  ngOnInit():void{
    this.formValue = this.formbuilder.group({
      text : [''],
      statusbar : [''],
      day : [''],
      workers : [''],
      description: [''],
      prerequisite:['']
    })
    this.AccessToken = this.api.AccessTokenThrow();

    //console.log(this.AccessToken)
    this.getAllTasks();

    this.milestoneValue = this.formbuilder.group({
      text : [''],
      description: [''],
      day : [''],
      statusbar : [''],
      tasks:['']
    })
    this.getAllMilestones();
    this.getAllProjects();
    this.getAllUsers();
    this.getProjectDetails();

  }

  setProjectId(row: any){
    let newId=row.id;
    this.projectId=newId;
    this.projectName=row.name;
    this.getAllTasks();
    this.getAllMilestones();
    this.getProjectDetails();
  }

  setTaskPageNumber(x:any ) {
    let temp = this.TaskPageNumber+x;
    if(temp>0 )
      this.TaskPageNumber+=x;
    this.getAllTasks;
    
  }
  setMilestonePageNumber(x:any ) {
    let temp = this.MilestonePageNumber+x;
    if(temp>0 )
      this.MilestonePageNumber+=x;
    this.getAllMilestones;
  }

  Archive: boolean=false;
  setToArchive(){
    this.Archive=true;
  }
  setToActive(){
    this.Archive=false;
  }




  postTaskDeatails(){

    const reqBody = {
      projectId: this.projectId,
      name: this.formValue.value.text,
      description: this.formValue.value.description,
      status: this.formValue.value.statusbar,
      deadline: this.formValue.value.day,
      assigneeIds: this.workersOfTask,
      prerequisiteTaskIds: this.prerequisitesOfTask
    }

    //console.log(reqBody);
    
    this.api.postTask(reqBody,this.projectId,this.AccessToken).subscribe(res=>{
      alert("Task added succesfully!");
      let ref = document.getElementById('close');
      ref?.click();
      this.formValue.reset();

      this.getAllTasks();
    },
    err=>{
      alert("Something went wrong!")
    }
    )
    this.workersOfTask.splice(0)
    this.prerequisitesOfTask.splice(0)
  }

  TaskWorkers!: any
  getAllTasks(){
    this.api.getTask(this.projectId,this.AccessToken).subscribe(res=>{
      this.taskData = res;
      console.log(this.taskData);
      
      //this.TaskWorkers = res
      //console.log(this.TaskWorkers.assigneeIds);
    })
  }

  projectDetails !: any
  tasksOfProject !: any
  getProjectDetails(){
    this.api.getProjectData(this.projectId,this.AccessToken).subscribe(res=>{
      this.projectDetails = res;
      //console.log(this.projectDetails);
      this.tasksOfProject = this.projectDetails.tasks
    })
    
  }

  deleteTask(row: any){
    
    this.api.deleteTask(row.id,this.projectId,this.AccessToken).subscribe(
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
    this.formValue.controls['text'].setValue(row.name);
    this.formValue.controls['statusbar'].setValue(row.status);
    this.formValue.controls['day'].setValue(row.deadline);
    //this.formValue.controls['worker'].setValue(row.assignees);
    this.formValue.controls['description'].setValue(row.description);
    this.formValue.controls['prerequisiteTaskIds'].setValue(row.prerequisite);
  }

  clickAddTask(){
    this.formValue.reset();
      this.showAdd=true;
      this.showUpdate=false;
  }

  updateTask(){

    const reqBody = {
      name: this.formValue.value.text,
      description: this.formValue.value.description,
      deadline: this.formValue.value.day,
      status: this.formValue.value.statusbar ,
      //assigneeIds: [],
      //prerequisiteTaskIds: [],
    }
    this.api.updateTask(reqBody,this.taskObject.id,this.projectId,this.AccessToken).subscribe(
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
      projectId: this.projectId,
      name: this.milestoneValue.value.text,
      description: this.milestoneValue.value.description,
      deadline: this.milestoneValue.value.day,
    }

    this.api.postMilestone(reqBody,this.projectId).subscribe(res=>{
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
      this.api.getProjectData(this.projectId,this.AccessToken).subscribe(res=>{
        this.milestoneData = res.milestones;
      })
    }
    deleteMilestone(row: any){
      this.api.deleteMilestone(row.id,this.projectId).subscribe(
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
    archiveTask(row:any, Archivation:boolean){
      const reqBody={
        archived: Archivation
      }
      this.api.archiveTask(reqBody,row.id,this.projectId,this.AccessToken).subscribe(
        res=>{
          this.getAllTasks();
        }
      )
      this.getAllTasks();
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
      this.api.updateMilestone(reqBody,this.milestoneObject.id,this.projectId).subscribe(
        res=>{
          alert("Update successful!");
          let ref = document.getElementById('close');
          ref?.click();
          this.milestoneValue.reset();
          this.getAllMilestones();
        }
      
      )
    }
    projectData!: any;
    getAllProjects(){
      this.api.getAllProjects(this.AccessToken).subscribe(res=>{
        this.projectData = res
        //console.log(this.projectData)
      })
    }

    userData!:any;
    getAllUsers(){
      this.api.getAllUsers(this.AccessToken).subscribe(res=>{
        this.userData = res
        //console.log(this.userData);
        
      })
    }

    workersOfTask : Array<number> = [];
    WorkerAdded(worker:number){ 

      //console.log(worker);
      
      //this.workersOfTask += worker
      //this.workersOfTask.length++
      this.workersOfTask.push(worker)
      //console.log(this.workersOfTask);
      
    }

    prerequisitesOfTask : Array<number> = [];
    PrerequisiteAdded(task:any){ 

      this.workersOfTask.push(task)
      
    }

}
