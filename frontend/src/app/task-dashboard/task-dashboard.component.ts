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
    //this.projectName = this.projectDetails.name

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

    //console.log(reqBody.assigneeIds);
    //console.log(reqBody);
    
    this.formValue.reset();
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
      //console.log(this.taskData);
      
      for(let row of this.taskData){
        let temp: String = ''
        let temp2: String = ''
        for(let subrow of row.assignees){
          temp = temp+ subrow.fullname+' ,'
        }
        for(let subrow of row.prerequisiteTasks){
          temp2= temp2+  subrow.name+' ,'
        }
        row.assignees = temp;
        row.prerequisiteTasks = temp2;
        
        
      }
    })
  }

  projectDetails !: any
  getProjectDetails(){
    this.api.getProjectData(this.projectId,this.AccessToken).subscribe(res=>{
      this.projectDetails = res;
      //console.log(this.projectDetails);

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
    this.formValue.reset()
    this.taskObject.id = row.id;
    this.formValue.controls['text'].setValue(row.name);
    this.formValue.controls['statusbar'].setValue(row.status);
    this.formValue.controls['day'].setValue(row.deadline);
    this.formValue.controls['description'].setValue(row.description);
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
      status: this.formValue.value.statusbar,
      assigneeIds: this.workersOfTask,
      prerequisiteTaskIds: this.prerequisitesOfTask,
    }
    this.formValue.reset();
    this.api.updateTask(reqBody,this.taskObject.id,this.projectId,this.AccessToken).subscribe(
      res=>{
        alert("Update succesful!");
        let ref = document.getElementById('close');
        ref?.click();
        this.formValue.reset();
        this.getAllTasks();
      }
    
    )
    this.workersOfTask.splice(0)
    this.prerequisitesOfTask.splice(0)
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
      taskIds: this.requiredTasks
    }

    this.api.postMilestone(reqBody,this.projectId,this.AccessToken).subscribe(res=>{
        alert("Milestone added succesfully!");
        let ref = document.getElementById('close');
        ref?.click();
        this.milestoneValue.reset();
        this.getAllMilestones();
    },
    err=>{
      alert("Something went wrong!")
    }
    )
    this.requiredTasks.splice(0)
  }

    getAllMilestones(){
      this.api.getMilestone(this.projectId,this.AccessToken).subscribe(res=>{
        this.milestoneData = res;
        

        for(let row of this.milestoneData){
          let temp: String = ''
          for(let subrow of row.tasks){
            
            temp = temp+ subrow.name +' ,'
          }
          row.tasks = temp;
        }
      })
    }
    deleteMilestone(row: any){
      this.api.deleteMilestone(row.id,this.projectId,this.AccessToken).subscribe(
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

    archiveMilestone(row:any, Archivation:boolean){
      const reqBody={
        archived: Archivation
      }
      this.api.archiveMilestone(reqBody,row.id,this.projectId,this.AccessToken).subscribe(
        res=>{
          this.getAllMilestones();
          
        }
      )
      this.getAllMilestones();
    }

    updateMilestone(){

      const reqBody = {
        name: this.milestoneValue.value.text,
        description: this.milestoneValue.value.description,
        deadline: this.milestoneValue.value.day,
        taskIds: this.requiredTasks
      }
      this.api.updateMilestone(reqBody,this.milestoneObject.id,this.projectId,this.AccessToken).subscribe(
        res=>{
          alert("Update successful!");
          let ref = document.getElementById('close');
          ref?.click();
          this.milestoneValue.reset();
          this.getAllMilestones();
        }
      
      )
      this.requiredTasks.splice(0)
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

    workersOfTask : Array<Number> = [];
    WorkerAdded(worker:Number){ 

      if(this.workersOfTask.includes(worker)){
        this.workersOfTask.forEach((element,index)=>{
          if(element==worker) this.workersOfTask.splice(index,1);
       });
       return;
      }
      this.workersOfTask.push(worker)
      //console.log(this.workersOfTask);
      
    }

    prerequisitesOfTask : Array<number> = [];
    PrerequisiteAdded(task:number){ 

      if(this.prerequisitesOfTask.includes(task)){
        this.prerequisitesOfTask.forEach((element,index)=>{
          if(element==task) this.prerequisitesOfTask.splice(index,1);
       });
       return;
      }
      this.prerequisitesOfTask.push(task)

      //console.log(this.prerequisitesOfTask);

      

    }

    requiredTasks: Array<number> = [];
    RequiredTaskAdded(task: number){

      if(this.requiredTasks.includes(task)){
        this.requiredTasks.forEach((element,index)=>{
          if(element==task) this.requiredTasks.splice(index,1);
       });
       return;
      }
      this.requiredTasks.push(task)
    }

    FinishingProject(){
      const reqBody={
        name: this.projectName,
        status: "Done"
      }
      this.api.FinishProject(reqBody,this.projectId,this.AccessToken).subscribe(
        res=>{
          alert("Finishing successful!");
          let ref = document.getElementById('close');
          ref?.click();
          this.getProjectDetails();
          this.getAllProjects();
        }
      
      )
      //console.log(this.projectData);
      
    }
}
