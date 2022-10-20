import { Injectable } from '@angular/core';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { identifierName } from '@angular/compiler';

@Injectable({
  providedIn: 'root'
})

export class ApiService {

  private apiAddress = "http://localhost:5000"
  constructor(private http: HttpClient) { }

  postTask(data: any){
    return this.http.post<any>(`${this.apiAddress}/tasks`,data).pipe(map((res:any)=>{return res;}))
  }
  getTask(){
    return this.http.get<any>(`${this.apiAddress}/tasks/`).pipe(map((res:any)=>{return res;}))
  }
  updateTask(data: any,id:number){
    return this.http.put<any>(`${this.apiAddress}/tasks/`+id,data).pipe(map((res:any)=>{return res;}))
  }
  deleteTask(id:number){
    return this.http.delete(`${this.apiAddress}/tasks/`+id, {responseType: 'text'}).pipe(map((res:any)=>{return res;}))
  }
}
