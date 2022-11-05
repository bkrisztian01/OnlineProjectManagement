import { Injectable } from '@angular/core';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { identifierName } from '@angular/compiler';

@Injectable({
  providedIn: 'root'
})

export class ApiService {

  private apiAddress = "https://opmapi.azurewebsites.net"
  constructor(private http: HttpClient) { }

  postTask(data: any){
    return this.http.post<any>(`${this.apiAddress}/tasks`,data).pipe(map((res:any)=>{return res;}))
  }
  getTask(){
    return this.http.get<any>(`${this.apiAddress}/tasks`).pipe(map((res:any)=>{return res;}))
  }
  updateTask(data: any,id:number){
    return this.http.put<any>(`${this.apiAddress}/tasks/`+id,data).pipe(map((res:any)=>{return res;}))
  }
  archiveTask(data:any,id:number){
    return this.http.put<any>(`${this.apiAddress}/tasks/`+id+`/archive`,data).pipe(map((res:any)=>{return res;}))
  }

  deleteTask(id:number){
    return this.http.delete(`${this.apiAddress}/tasks/`+id, {responseType: 'text'}).pipe(map((res:any)=>{return res;}))
  }
  postMilestone(data: any){
    return this.http.post<any>(`${this.apiAddress}/milestones`,data).pipe(map((res:any)=>{return res;}))
  }
  getMilestone(){
    return this.http.get<any>(`${this.apiAddress}/milestones`).pipe(map((res:any)=>{return res;}))
  }
  updateMilestone(data: any,id:number){
    return this.http.put<any>(`${this.apiAddress}/milestones/`+id,data).pipe(map((res:any)=>{return res;}))
  }
  deleteMilestone(id:number){
    return this.http.delete<any>(`${this.apiAddress}/milestones/`+id).pipe(map((res:any)=>{return res;}))
  }

  getProjectData(id:number){
    return this.http.get<any>(`${this.apiAddress}/projects/`+id).pipe(map((res:any)=>{return res;}))
  }
  getAllProjects(){
    return this.http.get<any>(`${this.apiAddress}/projects`).pipe(map((res:any)=>{return res;}))
  }

  getAllUsers(){
    return this.http.get<any>(`${this.apiAddress}/users`).pipe(map((res:any)=>{return res;}))
  }

}
