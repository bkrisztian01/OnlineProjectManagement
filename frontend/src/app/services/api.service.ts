import { Injectable } from '@angular/core';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { catchError, map, retry, timeout } from 'rxjs/operators';
import { identifierName } from '@angular/compiler';
import { throwError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})

export class ApiService {

  private apiAddress = "https://opmapi2.azurewebsites.net"

  private AccessToken!: String;
  private ProjectID!: number;

  constructor(private http: HttpClient) { }

  postTask(data: any, id: number, AccessToken: String){
    return this.http.post<any>(`/api/projects/`+id+`/tasks`,data,{headers:{"Authorization":"Bearer " + AccessToken}}).pipe(map(
      (res:any)=>{return res;})
      // ,
      // timeout(3000),
      // retry(4),
      // catchError((err)=> {
      //   this.refreshToken().subscribe(res=>{
      //     this.AccessToken=res;
      //     retry();
      //     console.error(err);
      //     return throwError(err);
      //   })
      // })
      )
  }
  getTask(id: number, AccessToken:String){
    return this.http.get<any>(`/api/projects/`+id+`/tasks`,{headers:{"Authorization":"Bearer " + AccessToken}}).pipe(map((res:any)=>{return res;}))
  }
  getTaskwithPage(id: number, AccessToken:String,pageNum:number){
    return this.http.get<any>(`/api/projects/`+id+`/tasks?pageNumber=`+pageNum,{headers:{"Authorization":"Bearer " + AccessToken}}).pipe(map((res:any)=>{return res;}))
  }
  updateTask(data: any,id:number, id2:number,AccessToken:String){
    return this.http.put<any>(`/api/projects/`+id2+`/tasks/`+id,data,{headers:{"Authorization":"Bearer " + AccessToken}}).pipe(map((res:any)=>{return res;}))
  }
  archiveTask(data:any,id:number, id2:number,AccessToken: String){
    return this.http.put<any>(`/api/projects/`+id2+`/tasks/`+id+`/archive`,data,{headers:{"Authorization":"Bearer " + AccessToken}}).pipe(map((res:any)=>{return res;}))
  }
  deleteTask(id:number,id2:number,AccessToken:String){
    return this.http.delete(`/api/projects/`+id2+`/tasks/`+id, {headers:{"Authorization":"Bearer " + AccessToken}}).pipe(map((res:any)=>{return res;}))
  }



  postMilestone(data: any,id:number,AccessToken:String){
    return this.http.post<any>(`/api/projects/`+id+`/milestones`,data, {headers:{"Authorization":"Bearer " + AccessToken}}).pipe(map((res:any)=>{return res;}))
  }
  getMilestone(id:number,AccessToken:String){
    return this.http.get<any>(`/api/projects/`+id+`/milestones`, {headers:{"Authorization":"Bearer " + AccessToken}}).pipe(map((res:any)=>{return res;}))
  }

  updateMilestone(data: any,id:number,id2:number,AccessToken:String){
    return this.http.put<any>(`/api/projects/`+id2+`/milestones/`+id,data, {headers:{"Authorization":"Bearer " + AccessToken}}).pipe(map((res:any)=>{return res;}))
  }
  deleteMilestone(id:number,id2:number,AccessToken:String){
    return this.http.delete<any>(`/api/projects/`+id2+`/milestones/`+id, {headers:{"Authorization":"Bearer " + AccessToken}}).pipe(map((res:any)=>{return res;}))
  }

  archiveMilestone(data:any,id:number, id2:number,AccessToken: String){
    return this.http.put<any>(`/api/projects/`+id2+`/milestones/`+id+`/archive`,data,{headers:{"Authorization":"Bearer " + AccessToken}}).pipe(map((res:any)=>{return res;}))
  }



  getProjectData(id:number, AccessToken:String){
    return this.http.get<any>(`/api/projects/`+id,{headers:{"Authorization":"Bearer " + AccessToken}}).pipe(map((res:any)=>{return res;}))
  }

  getAllProjects(AccessToken:String){
    return this.http.get<any>(`/api/projects`,{headers:{"Authorization":"Bearer " + AccessToken}}).pipe(map((res:any)=>{return res;}))
  }

  FinishProject(data: any,id:number,AccessToken:String){
    return this.http.put<any>(`/api/projects/`+id,data, {headers:{"Authorization":"Bearer " + AccessToken}}).pipe(map((res:any)=>{return res;}))
  }

  getAllUsers(AccessToken:String){
    return this.http.get<any>(`/api/users`,{headers:{"Authorization":"Bearer " + AccessToken}}).pipe(map((res:any)=>{return res;}))
  }

  postProject(data: any, AccessToken:String){
    return this.http.post<any>(`/api/projects`,data,{headers:{"Authorization":"Bearer " + AccessToken}}).pipe(map((res:any)=>{return res;}))
  }

  signIn(data:any){
    return this.http.post<any>(`/api/user/login`,data).pipe(map((res:any)=>{return res;}))
  }

  signOut(){
    return this.http.get<any>(`/api/user/logout`);
  }

  signUp(data:any){
    return this.http.post<any>(`/api/user/signup`,data).pipe(map((res:any)=>{return res;}))
  }
  refreshToken(){
  return this.http.get<any>(`/api/refresh`).pipe(map((res:any)=>{return res;}))
  }

  setAccessToken(data:String){
    this.AccessToken = data;
  }
  AccessTokenThrow(){
    return this.AccessToken;
  }

  setProjectID(ID:number){
    this.ProjectID=ID
  }
  ProjectIDThrow(){
    return this.ProjectID
  }


}
