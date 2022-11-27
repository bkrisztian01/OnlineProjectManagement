import { Component, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router, TitleStrategy } from '@angular/router';
import { Login } from '../model/login.model';
import { ApiService } from '../services/api.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  formValue !: FormGroup;
  login!: Login;
  @Output() AccessToken !:any;

  constructor(private formbuilder: FormBuilder, private api: ApiService, private route:ActivatedRoute, private router: Router){}

  ngOnInit(): void {
    this.formValue = this.formbuilder.group({
      username : [''],
      password : ['']
    })
  }


  NewLogin(){

    const reqBody = {
      username: this.formValue.value.username,
      password: this.formValue.value.password
    }

    console.log(reqBody)
    
    this.api.signIn(reqBody).subscribe(res=>{
      this.router.navigate(
        ["/dashboard"]
      );
      alert("Logged in succesfully!");
      this.AccessToken = res.accessToken;
      this.api.setAccessToken(this.AccessToken);
      localStorage.setItem('token',this.AccessToken)
      
      let ref = document.getElementById('close');
      ref?.click();
      this.formValue.reset();
    },
    err=>{
      alert("Something went wrong!");
      this.router.navigate(
        ["/login"]
      );
    }
    )
    console.log(this.route)
  }

}
