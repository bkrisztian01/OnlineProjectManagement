import { animate, style, transition, trigger } from '@angular/animations';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { signUp } from '../model/signup.model';
import { ApiService } from '../services/api.service';

const enterTransition = transition(':enter',[
  style({
    opacity: 0
  }),
  animate('1s ease-in',style({opacity:1}))
])
const fadeIn= trigger('fadeIn',[enterTransition])

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css'],
  animations: [fadeIn]
})
export class SignupComponent implements OnInit {
  formValue!: FormGroup;
  signUp!: signUp;

  constructor(private formbuilder: FormBuilder, private api: ApiService) {}

  ngOnInit(): void {
    this.formValue = this.formbuilder.group({
      username: [''],
      fullname: [''],
      email: [''],
      password: [''],
    });
  }

  NewSignUp() {
    const reqBody = {
      username: this.formValue.value.username,
      fullname: this.formValue.value.fullname,
      email: this.formValue.value.email,
      password: this.formValue.value.password,
    };

    this.api.signUp(reqBody).subscribe(
      (res) => {
        alert('Signed up succesfully!');
        let ref = document.getElementById('close');
        ref?.click();
        this.formValue.reset();
      },
      (err) => {
        alert('Something went wrong!');
      }
    );
  }
}
