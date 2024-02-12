import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { RegistrationModel } from 'src/app/interfaces/registration-model';
import { AuthService } from 'src/app/services/auth.service';
import { passwordMatchValidator } from 'src/app/validators/password-match.validator';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {

  registerForm:any

  constructor(
    private fb: FormBuilder,
    private authService:AuthService,
    private messageService:MessageService,
    private router:Router
  ) {
    this.registerForm = this.fb.group({
      fullname: ['', [Validators.required, Validators.pattern(/^[a-zA-Z]+(?: [a-zA-Z]+)*$/)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPass: ['', [Validators.required, Validators.minLength(6)]]
    })
    this.registerForm.setValidators(passwordMatchValidator)
  }

  get fullname() {
    return this.registerForm.controls['fullname']
  }

  get email() {
    return this.registerForm.controls['email']
  }

  get password() {
    return this.registerForm.controls['password']
  }

  get confirmPass() {
    return this.registerForm.controls['confirmPass']
  }

  submitDetails() {
    const postData = {...this.registerForm.value}
    delete postData.confirmPass
    this.authService.registerUser(postData as RegistrationModel).subscribe(
      response => {
        this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Registered successfully' })
        this.router.navigate(['login'])
      },
      error => {
        console.log(error);
        
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Something went wrong' })
      }
    )
  }
}
