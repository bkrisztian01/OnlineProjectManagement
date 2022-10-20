import { Injectable } from '@angular/core';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { identifierName } from '@angular/compiler';

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  constructor(private http: HttpClient) { }

  postTask(data: any){
    return this.http.post<any>("http://localhost:3000/posts",data).pipe(map((res:any)=>{return res;}))
  }
  getTask(){
    return this.http.get<any>("http://localhost:3000/posts").pipe(map((res:any)=>{return res;}))
  }
  updateTask(data: any,id:number){
    return this.http.put<any>("http://localhost:3000/posts/"+id,data).pipe(map((res:any)=>{return res;}))
  }
  deleteTask(id:number){
    return this.http.delete<any>("http://localhost:3000/posts/"+id).pipe(map((res:any)=>{return res;}))
  }

  postMilestone(data: any){
    return this.http.post<any>("http://localhost:3000/miles",data).pipe(map((res:any)=>{return res;}))
  }
  getMilestone(){
    return this.http.get<any>("http://localhost:3000/miles").pipe(map((res:any)=>{return res;}))
  }
  updateMilestone(data: any,id:number){
    return this.http.put<any>("http://localhost:3000/miles/"+id,data).pipe(map((res:any)=>{return res;}))
  }
  deleteMilestone(id:number){
    return this.http.delete<any>("http://localhost:3000/miles/"+id).pipe(map((res:any)=>{return res;}))
  }
}
