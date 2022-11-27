import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder,FormGroup } from '@angular/forms';
import { Task } from '../model/tasks.model';
import { ApiService } from '../services/api.service';
import { Milestone } from '../model/milestones.model';
import { User } from '../model/users.model';
import { Observable } from 'rxjs';
import { ApexAxisChartSeries, ApexChart, ApexLegend, ApexNonAxisChartSeries, ApexPlotOptions, ApexResponsive, ApexXAxis, ChartComponent } from 'ng-apexcharts';
// import { TaskObjects } from './task-dashboard.model';

export type ChartOptions = {
  series: ApexNonAxisChartSeries;
  chart: ApexChart;
  colors :string[],
  responsive: ApexResponsive[];
  labels: any;
};
export type ChartOptionsGantt = {
  series: ApexAxisChartSeries;
  chart: ApexChart;
  xaxis: ApexXAxis;
  plotOptions: ApexPlotOptions;
};
export type ChartOptionsRadial = {
  series: ApexNonAxisChartSeries;
  chart: ApexChart;
  labels: string[];
  colors: string[];
  legend: ApexLegend;
  plotOptions: ApexPlotOptions;
  responsive: ApexResponsive | ApexResponsive[];
};

@Component({
  selector: 'app-task-dashboard',
  templateUrl: './task-dashboard.component.html',
  styleUrls: ['./task-dashboard.component.css']
})


export class TaskDashboardComponent implements OnInit {

  formValue !: FormGroup;
  milestoneValue !: FormGroup;
  projectValue !: FormGroup;
  taskObject : Task = new Task();
  taskData !: any;
  showAdd!: boolean;
  showUpdate!: boolean;
  TaskPageNumber:any = 1;
  MilestonePageNumber = 1;

  AccessToken!: any;

  @ViewChild("chart") chart!: ChartComponent;
  public chartOptions!: Partial<ChartOptions> ;

  @ViewChild("chart") chart2!: ChartComponent;
  public ChartOptionsGantt!: Partial<ChartOptionsGantt> ;

  @ViewChild("chart") chart3!: ChartComponent;
  public ChartOptionsRadial!: Partial<ChartOptionsRadial> ;

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

    this.projectValue = this.formbuilder.group({
      name : [''],
      statusbar : [''],
      day : [''],
      workers : [''],
      description: [''],
      manager:[''],
      users:['']
    })
    
    this.milestoneValue = this.formbuilder.group({
      text : [''],
      description: [''],
      day : [''],
      statusbar : [''],
      tasks:['']
    })
    this.AccessToken = this.api.AccessTokenThrow();

    this.getAllTasks();
    this.getAllMilestones();
    this.getAllProjects();
    this.getAllUsers();
    this.getProjectDetails();
    //this.projectName = this.projectDetails.name
    this.api.setProjectID(this.projectId)


  }

  initializeChart(){
    this.chartOptions = {
      series: [],
      chart: {
        type: "donut",
        width: 500,
        height: 500
      },
      labels: [],
      colors: ["#289A09", "#ffc133", "#FF4233","#96a2a3"],
      responsive: [
        {
          breakpoint: 480,
          options: {
            chart: {
              width: 200,
              
            },
            legend: {
              position: "bottom"
            }
          }
        }
      ]
    };
    this.ChartOptionsRadial = {
      series: [],
      chart: {
        height: 368,
        type: "radialBar"
      },
      plotOptions: {
        radialBar: {
          offsetY: 0,
          startAngle: 0,
          endAngle: 270,
          hollow: {
            margin: 5,
            size: "30%",
            background: "transparent",
            image: undefined
          },
          dataLabels: {
            name: {
              show: false
            },
            value: {
              show: false
            }
          }
        }
      },
      colors: ["#289A09", "#ffc133", "#96a2a3", "#FF4233"],
      labels: ["Done", "In Progress", "Not Started", "Stopped"],
      legend: {
        show: true,
        floating: true,
        fontSize: "16px",
        position: "left",
        offsetX: 50,
        offsetY: 10,
        labels: {
          useSeriesColors: true
        },
        formatter: function(seriesName, opts) {
          return seriesName + ":  " + opts.w.globals.series[opts.seriesIndex];
        },
        itemMargin: {
          horizontal: 3
        }
      },
      responsive: [
        {
          breakpoint: 480,
          options: {
            legend: {
              show: false
            }
          }
        }
      ]
    };

      let finished=0
      let InProgress=0
      let Stopped=0
      let NotStarted=0

      for(let row of this.taskData){
        if(row.status == "Done") finished++
        else if(row.status == "In Progress") InProgress++
        else if(row.status == "Stopped") Stopped++
        else if(row.status == "Not Started") NotStarted++
      }
      this.chartOptions.labels = ["Done","In Progress","Stopped","Not Started"]
      this.chartOptions.series = [finished,InProgress,Stopped,NotStarted]
      this.ChartOptionsRadial.series = [finished,InProgress,NotStarted,Stopped]
    
  }

  initializeGanttChart(){
    this.ChartOptionsGantt = {
          series: [ ],
          chart: {
            height: 332,
            type: "rangeBar"
          },
          plotOptions: {
            bar: {
              horizontal: true,
              distributed: true,
            }
          },
          xaxis: {
            type: "datetime"
          }
        };

      let data:any[] = [];
      for(let row of this.taskData){

        let x:number = row.name;
        //let y1:Date  =row.startDate;
        let y1 = new Date(row.startDate).getTime()
        //let y2: Date = row.deadline;
        let y2 = new Date(row.deadline).getTime()
        let fillColor!: String
        if(row.status=="In Progress")  fillColor = "#ffc133"
        else if(row.status=="Stopped")  fillColor = "#ff4233"
        else if(row.status=="Done")  fillColor = "#289a09"
        else if(row.status=="Not Started")  fillColor = "#96a2a3"
        
        let y:number[] = [y1,y2]
        data.push({x,y,fillColor})

      }
      this.ChartOptionsGantt.series?.push(
        {data}
        )
        
  }

  setProjectId(row: any){
    let newId=row.id;
    this.projectId=newId;
    this.projectName=row.name;
    this.getAllTasks();
    this.getAllMilestones();
    this.getProjectDetails();
    this.api.setProjectID(this.projectId)
    
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


  postProject(){
    const reqBody = {
      name: this.projectValue.value.name,
      description: this.projectValue.value.description,
      status: this.projectValue.value.statusbar,
      estimatedTime: this.projectValue.value.day,
      managerId: Number(this.projectValue.value.manager),
      memberIds: this.membersOfProject
    }
    
    this.projectValue.reset();
    this.api.postProject(reqBody,this.AccessToken).subscribe(res=>{
      alert("Project created successfully!");
      let ref= document.getElementById('close');
      ref?.click();
      this.projectValue.reset();
      this.getAllProjects();
    },
    err=>{
      alert("Something went wrong!")
    }

    )
    this.membersOfProject.splice(0);
  }

  membersOfProject: Array<number> = [];
  ProjectMemberAdded(member: number){

    if(this.membersOfProject.includes(member)){
      this.membersOfProject.forEach((element,index)=>{
        if(element==member) this.membersOfProject.splice(index,1);
     });
     return;
    }
    this.membersOfProject.push(member);
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
      
      for(let row of this.taskData){
        let temp: String = ''
        let temp2: String = ''
        for(let subrow of row.assignees){
          temp = temp+ subrow.fullname+' ,'
        }
        for(let subrow of row.prerequisiteTasks){
          temp2= temp2+  subrow.name+' ,'
        }
        row.assignees = temp.slice(0,-1);
        row.prerequisiteTasks = temp2.slice(0,-1);
        
        if(this.taskData.length!=0) this.initializeChart();
        this.initializeGanttChart()

      }
    },
    (error)=>{
      this.RefreshingToken();
      this.getAllTasks();
    }
    )
  }

  RefreshingToken(){
    this.api.refreshToken().subscribe(res=>{
      this.AccessToken = res.accessToken
      console.log(this.AccessToken);
      }
      
      
    )
  }

  projectDetails !: any
  getProjectDetails(){
    this.api.getProjectData(this.projectId,this.AccessToken).subscribe(res=>{
      this.projectDetails = res;
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
          row.tasks = temp.slice(0,-1);
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
      })
    }

    userData!:any;
    getAllUsers(){
      this.api.getAllUsers(this.AccessToken).subscribe(res=>{
        this.userData = res
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
      
    }
}
