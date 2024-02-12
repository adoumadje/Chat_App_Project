import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { ForgotModel } from 'src/app/interfaces/forgot-model';
import { ResetToken } from 'src/app/interfaces/reset-token';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent {
  forgotForm:any

  constructor(
    private fb:FormBuilder,
    private authService:AuthService,
    private messageService:MessageService,
    private router:Router
    ) {
    this.forgotForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]]
    })
  }

  get email() {
    return this.forgotForm.controls['email']
  }

  submitDetails() {
    const postData = {...this.forgotForm.value}
    
    this.authService.forgotPassword(postData as ForgotModel).subscribe(
      response => {
        const token = response as ResetToken
        sessionStorage.setItem('passwordResetToken', token.passwordResetToken)
        this.router.navigate(['reset-password'])
      },
      error => {
        console.log(error);
        
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Invalid Email'})
      }
    )
  }
}
