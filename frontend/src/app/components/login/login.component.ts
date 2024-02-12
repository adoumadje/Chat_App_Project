import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { LoginModel } from 'src/app/interfaces/login-model';
import { User } from 'src/app/interfaces/user';
import { AuthService } from 'src/app/services/auth.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  loginForm:any

  constructor(
    private fb:FormBuilder,
    private authService:AuthService,
    private messageService:MessageService,
    private userService:UserService,
    private router:Router
    ) {
      this.loginForm = this.fb.group({
        email: ['', [Validators.required, Validators.email]],
        password: ['', [Validators.required, Validators.minLength(6)]]
      })
  }

  get email() {
    return this.loginForm.controls['email']
  }

  get password() {
    return this.loginForm.controls['password']
  }

  submitDetails() {
    const postData = {...this.loginForm.value}
    this.authService.loginUser(postData as LoginModel).subscribe(
      response => {
        const user = response as User
        this.userService.setUser(user)
        sessionStorage.setItem('user', JSON.stringify(user))
        sessionStorage.setItem('userToken', user.token)
        this.router.navigate(['chat'], { replaceUrl: true })
      },
      error => {
        console.log(error);
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Something went wrong'})
      }
    )
  }
}
