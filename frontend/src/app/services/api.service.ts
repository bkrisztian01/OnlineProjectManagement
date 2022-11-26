import { Injectable } from '@angular/core';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { identifierName } from '@angular/compiler';

@Injectable({
  providedIn: 'root'
})

export class ApiService {

  private apiAddress = "https://opmapi.azurewebsites.net"

  private AccessToken!: String;

  constructor(private http: HttpClient) { }

  postTask(data: any, id: number, AccessToken: String){
    return this.http.post<any>(`${this.apiAddress}/projects/`+id+`/tasks`,data,{headers:{"Authorization":"Bearer " + AccessToken}}).pipe(map((res:any)=>{return res;}))
  }
  getTask(id: number, AccessToken:String){
    return this.http.get<any>(`${this.apiAddress}/projects/`+id+`/tasks`,{headers:{"Authorization":"Bearer " + AccessToken}}).pipe(map((res:any)=>{return res;}))
  }
  updateTask(data: any,id:number, id2:number,AccessToken:String){
    return this.http.put<any>(`${this.apiAddress}/projects/`+id2+`/tasks/`+id,data,{headers:{"Authorization":"Bearer " + AccessToken}}).pipe(map((res:any)=>{return res;}))
  }
  archiveTask(data:any,id:number, id2:number,AccessToken: String){
    return this.http.put<any>(`${this.apiAddress}/projects/`+id2+`/tasks/`+id+`/archive`,data,{headers:{"Authorization":"Bearer " + AccessToken}}).pipe(map((res:any)=>{return res;}))
  }
  deleteTask(id:number,id2:number,AccessToken:String){
    return this.http.delete(`${this.apiAddress}/projects/`+id2+`/tasks/`+id, {headers:{"Authorization":"Bearer " + AccessToken}}).pipe(map((res:any)=>{return res;}))
  }



  postMilestone(data: any,id:number,AccessToken:String){
    return this.http.post<any>(`${this.apiAddress}/projects/`+id+`/milestones`,data, {headers:{"Authorization":"Bearer " + AccessToken}}).pipe(map((res:any)=>{return res;}))
  }
  getMilestone(id:number,AccessToken:String){
    return this.http.get<any>(`${this.apiAddress}/projects/`+id+`/milestones`, {headers:{"Authorization":"Bearer " + AccessToken}}).pipe(map((res:any)=>{return res;}))
  }

  updateMilestone(data: any,id:number,id2:number,AccessToken:String){
    return this.http.put<any>(`${this.apiAddress}/projects/`+id2+`/milestones/`+id,data, {headers:{"Authorization":"Bearer " + AccessToken}}).pipe(map((res:any)=>{return res;}))
  }
  deleteMilestone(id:number,id2:number,AccessToken:String){
    return this.http.delete<any>(`${this.apiAddress}/projects/`+id2+`/milestones/`+id, {headers:{"Authorization":"Bearer " + AccessToken}}).pipe(map((res:any)=>{return res;}))
  }

  archiveMilestone(data:any,id:number, id2:number,AccessToken: String){
    return this.http.put<any>(`${this.apiAddress}/projects/`+id2+`/milestones/`+id+`/archive`,data,{headers:{"Authorization":"Bearer " + AccessToken}}).pipe(map((res:any)=>{return res;}))
  }



  getProjectData(id:number, AccessToken:String){
    return this.http.get<any>(`${this.apiAddress}/projects/`+id,{headers:{"Authorization":"Bearer " + AccessToken}}).pipe(map((res:any)=>{return res;}))
  }

  getAllProjects(AccessToken:String){
    return this.http.get<any>(`${this.apiAddress}/projects`,{headers:{"Authorization":"Bearer " + AccessToken}}).pipe(map((res:any)=>{return res;}))
  }

  FinishProject(data: any,id:number,AccessToken:String){
    return this.http.put<any>(`${this.apiAddress}/projects/`+id,data, {headers:{"Authorization":"Bearer " + AccessToken}}).pipe(map((res:any)=>{return res;}))
  }

  getAllUsers(AccessToken:String){
    return this.http.get<any>(`${this.apiAddress}/users`,{headers:{"Authorization":"Bearer " + AccessToken}}).pipe(map((res:any)=>{return res;}))
  }

  postProject(data: any){
    return this.http.post<any>(`${this.apiAddress}/project`,data).pipe(map((res:any)=>{return res;}))
  }

  signIn(data:any){
    return this.http.post<any>(`${this.apiAddress}/user/login`,data).pipe(map((res:any)=>{return res;}))
  }

  signUp(data:any){
    return this.http.post<any>(`${this.apiAddress}/user/signup`,data).pipe(map((res:any)=>{return res;}))
  }

  setAccessToken(data:String){
    this.AccessToken = data;
  }
  AccessTokenThrow(){
    return this.AccessToken;
  }


}
